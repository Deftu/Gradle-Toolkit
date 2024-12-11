package dev.deftu.gradle.tools.minecraft

import dev.deftu.gradle.ToolkitConstants
import dev.deftu.gradle.utils.*
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.*
import java.io.File
import java.util.*

abstract class LoomHelperExtension(
    val project: Project
) {

    internal var usingKotlinForForge = false
        private set

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

    fun useOneConfig(version: MinecraftVersion, loader: ModLoader, vararg modules: String) {
        val repos = arrayOf("https://repo.polyfrost.org/releases", "https://repo.polyfrost.org/snapshots")
        project.repositories {
            repos.forEach { maven(it) }
        }

        val mcData = MCData.from(project)

        // Set up OneConfig's loader
        val loaderModule = when {
            mcData.isFabric -> "fabriclike"
            mcData.isLegacyForge -> "launchwrapper"
            else -> "modlauncher"
        }

        val loaderDependency = "org.polyfrost.oneconfig:stage0"

        val cacheDir = File(project.gradle.projectCacheDir, ".oneconfig-version-cache").apply { mkdirs() }
        val globalCacheDir = File(ToolkitConstants.dir, ".oneconfig-version-cache").apply { mkdirs() }

        val cachedLoaderFilename = "${version}-${loaderModule}-STAGE0.txt"
        val loaderVersion =
            DependencyHelper.fetchLatestReleaseFromLocal(project, loaderDependency) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repos[0], loaderDependency, cacheDir.resolve(cachedLoaderFilename)) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repos[1], loaderDependency, cacheDir.resolve(cachedLoaderFilename)) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repos[0], loaderDependency, globalCacheDir.resolve(cachedLoaderFilename)) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repos[1], loaderDependency, globalCacheDir.resolve(cachedLoaderFilename)) ?:
            throw IllegalStateException("Failed to fetch latest OneConfig loader version.")

        val fullLoaderDependency = "$loaderDependency:$loaderVersion:$loaderModule"
        if (mcData.isFabric) {
            // JiJ (Jar-in-Jar) the loader
            project.dependencies.add("include", fullLoaderDependency)
        } else {
            // Embed the loader
            val usingShadow = project.pluginManager.hasPlugin("dev.deftu.gradle.tools.shadow")
//            project.dependencies.add(if (usingShadow) "shade" else "implementation", fullLoaderDependency)

            if (usingShadow) {
                project.dependencies.add("shade", fullLoaderDependency)
            }

            project.dependencies.add("implementation", fullLoaderDependency)

            if (!usingShadow) {
                project.logger.warn("It is recommended to use DGT Shadow to embed the OneConfig loader inside your built mod JAR.")
            }
        }

        // Set up OneConfig dependencies
        val dependencies = modules.map { module -> module to false } + ("$version-$loader" to true)
        for (dep in dependencies) {
            val cachedDependencyFilename = "${dep.first}-ONECONFIG.txt"
            val dependency = "org.polyfrost.oneconfig:${dep.first}"
            val moduleVersion =
                DependencyHelper.fetchLatestReleaseFromLocal(project, dependency) ?:
                DependencyHelper.fetchLatestReleaseOrCached(repos[0], dependency, cacheDir.resolve(cachedDependencyFilename)) ?:
                DependencyHelper.fetchLatestReleaseOrCached(repos[1], dependency, cacheDir.resolve(cachedDependencyFilename)) ?:
                DependencyHelper.fetchLatestReleaseOrCached(repos[0], dependency, globalCacheDir.resolve(cachedDependencyFilename)) ?:
                DependencyHelper.fetchLatestReleaseOrCached(repos[1], dependency, globalCacheDir.resolve(cachedDependencyFilename)) ?:
                throw IllegalStateException("Failed to fetch latest OneConfig version for module ${dep.first}.")
            project.dependencies.add(if (dep.second) "modCompileOnly" else "compileOnly", "$dependency:$moduleVersion")
        }
    }

    fun useOneConfig(mcData: MCData, vararg modules: String) {
        useOneConfig(mcData.version, mcData.loader, *modules)
    }

    /**
     * Allows you to use DevAuth while in the development environment.
     */
    fun useDevAuth() {
        val repo = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1"
        project.repositories.maven {
            url = project.uri(repo)
        }

        val mcData = MCData.from(project)

        val cacheDir = File(project.gradle.projectCacheDir, ".devauth-version-cache").apply { mkdirs() }
        val globalCacheDir = File(ToolkitConstants.dir, ".devauth-version-cache").apply { mkdirs() }

        val module = if (mcData.isFabric) "fabric" else if (mcData.isForge && mcData.version <= MinecraftVersion.VERSION_1_12_2) "forge-legacy" else "forge-latest"
        val dependency = "me.djtheredstoner:DevAuth-$module"
        val version =
            DependencyHelper.fetchLatestReleaseFromLocal(project, dependency) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repo, dependency, cacheDir.resolve("$module.txt")) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repo, dependency, globalCacheDir.resolve("$module.txt")) ?:
            throw IllegalStateException("Failed to fetch latest DevAuth version.")
        project.dependencies.add("modRuntimeOnly", "$dependency:$version")
    }

}
