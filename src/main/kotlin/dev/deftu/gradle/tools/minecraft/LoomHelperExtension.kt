package dev.deftu.gradle.tools.minecraft

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import dev.deftu.gradle.GameInfo
import dev.deftu.gradle.MCData
import dev.deftu.gradle.utils.GameSide
import dev.deftu.gradle.utils.withLoom
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.utils.property
import java.util.*

abstract class LoomHelperExtension(
    val project: Project
) {
    internal var usingKotlinForForge = false
        private set

    abstract val appleSiliconFix: Property<Boolean>

    init {
        appleSiliconFix.convention(true)
    }

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
                GameSide.GLOBAL -> runConfigs.all { programArgs(key, value) }
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
                GameSide.GLOBAL -> runConfigs.all { property(key, value) }
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
                    if (mcData.isLegacyForge) theAttributes.apply {
                        this["ForceLoadAsMod"] = true
                        this["TweakOrder"] = "0"
                        if (side != GameSide.GLOBAL) this["Side"] = side.name.lowercase(Locale.US)
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
                    if (mcData.isLegacyForge) theAttributes.apply {
                        this["ForceLoadAsMod"] = true
                        this["FMLCorePluginContainsFMLMod"] = true
                        if (side != GameSide.GLOBAL) this["Side"] = side.name.lowercase(Locale.US)
                    }
                    attributes(theAttributes)
                }
            }
        }
    }

    @JvmOverloads
    fun useKotlinForForge(notation: String = "thedarkcolour:kotlinforforge:") = apply {
        val mcData = MCData.from(project)
        if (mcData.present) {
            val version = GameInfo.fetchKotlinForForgeVersion(mcData.version)

            project.repositories {
                maven("https://thedarkcolour.github.io/KotlinForForge/")
            }

            project.dependencies {
                val finalNotation = if (notation.endsWith(':')) notation else "$notation:"
                add("implementation", "$finalNotation$version")
            }

            usingKotlinForForge = true
        }
    }

    /**
     * Disables game run configs for
     * the specified game state.
     */
    fun disableRunConfigs(side: GameSide) = apply {
        project.withLoom {
            when (side) {
                GameSide.GLOBAL -> runConfigs.all { isIdeConfigGenerated = false }
                else -> runConfigs[side.name.lowercase(Locale.US)].isIdeConfigGenerated = false
            }
        }
    }
}
