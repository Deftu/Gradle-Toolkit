package dev.deftu.gradle.tools.minecraft

import dev.deftu.gradle.ToolkitConstants
import dev.deftu.gradle.utils.*
import dev.deftu.gradle.utils.version.MinecraftVersions
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import java.io.File
import java.util.*

abstract class LoomHelperExtension(
    val project: Project
) {

    internal var usingKotlinForForge = false
        private set

    private var usingOneConfig = false

    /**
     * Sets a Mixin config for
     * Forge to use.
     */
    @JvmOverloads
    fun useForgeMixin(namespace: String, file: Boolean = false) {
        val value = if (file) namespace else "mixins.$namespace.json"
        project.withLoom {
            forge {
                mixinConfig(value)
            }
        }

        project.pluginManager.withPlugin("java") {
            project.tasks.withType<Jar> {
                manifest {
                    attributes(mapOf(
                        "MixinConfigs" to value
                    ))
                }
            }
        }
    }

    @JvmOverloads
    fun useMixinRefMap(namespace: String, file: Boolean = false) {
        val value = if (file) namespace else "mixins.$namespace.refmap.json"
        project.withLoom {
            mixin.defaultRefmapName.set(value)
        }
    }

    /**
     * Makes Loom use a command-line
     * argument.
     */
    fun useArgument(key: String, value: String, side: GameSide) = apply {
        project.withLoom {
            when (side) {
                GameSide.BOTH -> runConfigs.all { programArgs(key, value) }
                else -> runConfigs[side.name.lowercase(Locale.US)].programArgs(key, value)
            }
        }
    }

    /**
     * Makes Loom use a VM
     * property/environment variable.
     */
    fun useProperty(key: String, value: String, side: GameSide) = apply {
        project.withLoom {
            when (side) {
                GameSide.BOTH -> runConfigs.all { property(key, value) }
                else -> runConfigs[side.name.lowercase(Locale.US)].property(key, value)
            }
        }
    }

    /**
     * Appends a tweaker to your mod.
     */
    fun useTweaker(
        value: String,
        side: GameSide = GameSide.CLIENT
    ) = apply {
        val mcData = MCData.from(project)
        useArgument("--tweakClass", value, side)
        project.pluginManager.withPlugin("java") {
            project.tasks.withType<Jar> {
                manifest {
                    val theAttributes = mutableMapOf<String, Any>(
                        "TweakClass" to value
                    )

                    if (mcData.isLegacyForge)
                        theAttributes.apply {
                            this["ForceLoadAsMod"] = true
                            this["TweakOrder"] = "0"
                            if (side != GameSide.BOTH) this["Side"] = side.name.lowercase(Locale.US)
                        }

                    attributes(theAttributes)
                }
            }
        }
    }

    fun useCoreMod(
        value: String,
        side: GameSide = GameSide.CLIENT
    ) = apply {
        val mcData = MCData.from(project)
        useProperty("fml.coreMods.load", value, side)
        project.pluginManager.withPlugin("java") {
            project.tasks.withType<Jar> {
                manifest {
                    val theAttributes = mutableMapOf<String, Any>(
                        "FMLCorePlugin" to value
                    )

                    if (mcData.isLegacyForge)
                        theAttributes.apply {
                            this["ForceLoadAsMod"] = true
                            this["FMLCorePluginContainsFMLMod"] = true
                            if (side != GameSide.BOTH) this["Side"] = side.name.lowercase(Locale.US)
                        }

                    attributes(theAttributes)
                }
            }
        }
    }

    fun useKotlinForForge() = apply {
        val mcData = MCData.from(project)
        if (mcData.isPresent) {
            val notation = "thedarkcolour:kotlinforforge"

            project.repositories {
                maven("https://thedarkcolour.github.io/KotlinForForge/")
            }

            project.dependencies {
                val finalNotation = buildString {
                    append(notation.removeSuffix(":"))
                    if (mcData.isNeoForge) append("-neoforge")
                }

                add("implementation", "$finalNotation:${mcData.dependencies.forgeLike.kotlinForForgeVersion}") {
                    // LexForge
                    exclude(group = "net.minecraftforge.fmlloader")

                    // NeoForge
                    exclude(group = "net.neoforged.fancymodloader", module = "loader")
                }
            }

            usingKotlinForForge = true
        }
    }

    /**
     * Disables game run configs for the specified game side.
     */
    fun disableRunConfigs(side: GameSide) = apply {
        project.withLoom {
            when (side) {
                GameSide.BOTH -> runConfigs.all { isIdeConfigGenerated = false }
                else -> runConfigs[side.name.lowercase(Locale.US)].isIdeConfigGenerated = false
            }
        }
    }

    fun useOneConfig(block: OneConfigBuilder.() -> Unit) {
        usingOneConfig = true

        val mcData = MCData.from(project)
        val minecraftVersion = mcData.version
        val modLoader = mcData.loader

        fun isUsingLoader(): Boolean {
            return modLoader == ModLoader.FORGE && minecraftVersion <= MinecraftVersions.VERSION_1_12_2
        }

        val builder = OneConfigBuilder().apply(block)
        if (isUsingLoader() && builder.loaderVersion == null) {
            throw NullPointerException("loaderVersion must be set when using Forge 1.12.2 or lower.")
        }

        val repos = arrayOf("Polyfrost Releases" to "https://repo.polyfrost.org/releases", "Polyfrost Snapshots" to "https://repo.polyfrost.org/snapshots")
        project.repositories {
            for ((name, url) in repos) {
                maven(url) {
                    this.name = name
                }
            }
        }

        if (isUsingLoader()) {
            val loaderModule = "all" // Shadowed dependencies
            val loaderDependency = "org.polyfrost.oneconfig:stage0"
            val fullLoaderDependency = "$loaderDependency:${builder.loaderVersion}:$loaderModule"
            val usingShadow = project.pluginManager.hasPlugin("dev.deftu.gradle.tools.shadow")
            if (usingShadow) {
                project.dependencies.add("shade", fullLoaderDependency)
            } else {
                project.logger.warn("It is recommended to use DGT Shadow to embed the OneConfig loader inside your built mod JAR.")
            }

            project.dependencies.add("implementation", fullLoaderDependency)

            if (builder.applyLoaderTweaker) {
                useTweaker("org.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker", GameSide.CLIENT)
            }
        }

        if (builder.usePolyMixin && isUsingLoader()) {
            val polyMixinVersion = builder.polyMixinVersion ?: throw NullPointerException("polyMixinVersion must be set when using PolyMixin.")
            val polyMixinDependency = "org.polyfrost:polymixin"
            project.dependencies.add(if (isUsingLoader()) "compileOnly" else "implementation", "$polyMixinDependency:$polyMixinVersion")
        }

        val dependencies = builder.modules.map { it to false } + ("${minecraftVersion}-${modLoader}" to true)
        for ((dep, isMod) in dependencies) {
            val dependency = "org.polyfrost.oneconfig:$dep"
            val configuration = if (isMod) {
                if (isUsingLoader()) "modCompileOnly" else "modImplementation"
            } else {
                if (isUsingLoader()) "compileOnly" else "implementation"
            }

            project.dependencies.add(configuration, "$dependency:${builder.version}")
        }
    }

    fun useMixinExtras(version: String) {
        val repository = "https://jitpack.io"
        project.repositories.maven {
            url = project.uri(repository)
        }

        val mcData = MCData.from(project)
        val dependencies = "io.github.llamalad7:mixinextras-common"
        project.dependencies.add(if (usingOneConfig && mcData.loader == ModLoader.FORGE && mcData.version <= MinecraftVersions.VERSION_1_12_2) "modCompileOnly" else "modImplementation", "$dependencies:$version")
        project.dependencies.add("annotationProcessor", "$dependencies:$version")
    }

    /**
     * Allows you to use DevAuth while in the development environment.
     */
    fun useDevAuth(version: String) {
        val repo = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1"
        project.repositories.maven {
            url = project.uri(repo)
        }

        val mcData = MCData.from(project)

        val module = if (mcData.isFabric) "fabric" else if (mcData.isForge && mcData.version <= MinecraftVersions.VERSION_1_12_2) "forge-legacy" else "forge-latest"
        val dependency = "me.djtheredstoner:DevAuth-$module"
        project.dependencies.add("modRuntimeOnly", "$dependency:$version")
    }

    /**
     * Adds Essential as a dependency, also shading/bundling the loader
     */
    fun useEssential() {
        val repo = "https://repo.essential.gg/repository/maven-public"
        project.repositories.maven {
            url = project.uri(repo)
        }

        val mcData = MCData.from(project)
        val loaderDependency = "gg.essential:" + if (mcData.isForge) "loader-launchwrapper" else "loader-fabric"

        val cacheDir = File(project.gradle.projectCacheDir, ".essential-version-cache").apply { mkdirs() }
        val globalCacheDir = File(ToolkitConstants.dir, ".essential-version-cache").apply { mkdirs() }

        val cachedLoaderFilename = "${mcData.version}-${mcData.loader.friendlyString}-LOADER.txt"
        val loaderVersion =
            DependencyHelper.fetchLatestReleaseFromLocal(project, loaderDependency) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repo, loaderDependency, cacheDir.resolve(cachedLoaderFilename)) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repo, loaderDependency, globalCacheDir.resolve(cachedLoaderFilename)) ?:
            throw IllegalStateException("Failed to fetch latest Essential loader version.")

        if (mcData.isFabric) {
            // JiJ (Jar-in-Jar) the loader
            project.dependencies.add("include", "$loaderDependency:$loaderVersion")
        } else {
            // Embed the loader
            val usingShadow = project.pluginManager.hasPlugin("dev.deftu.gradle.tools.shadow")

            if (usingShadow) {
                project.dependencies.add("shade", "$loaderDependency:$loaderVersion")
            }

            project.dependencies.add("implementation", "$loaderDependency:$loaderVersion")

            if (!usingShadow) {
                project.logger.warn("It is recommended to use DGT Shadow to embed the Essential loader inside your built mod JAR.")
            }
        }

        val cachedApiFilename = "${mcData.version}-${mcData.loader.friendlyString}-API.txt"
        val apiDependency = "gg.essential:essential-${mcData.version}-${mcData.loader.friendlyString}"
        val apiVersion =
            DependencyHelper.fetchLatestReleaseFromLocal(project, apiDependency) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repo, apiDependency, cacheDir.resolve(cachedApiFilename)) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repo, apiDependency, globalCacheDir.resolve(cachedApiFilename)) ?:
            throw IllegalStateException("Failed to fetch latest Essential API version.")
        project.dependencies.add("compileOnly", "$apiDependency:$apiVersion")
    }

}
