package xyz.deftu.gradle.tools.minecraft

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.utils.GameSide

abstract class LoomHelperExtension(
    val project: Project
) {
    /**
     * Sets a Mixin config for
     * Forge to use.
     */
    @JvmOverloads
    fun useForgeMixin(namespace: String, file: Boolean = false) {
        val value = if (file) namespace else "mixins.$namespace.json"
        withLoom {
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
        withLoom {
            mixin.defaultRefmapName.set(value)
        }
    }

    /**
     * Makes Loom use a command-line
     * argument.
     */
    fun useArgument(key: String, value: String, side: GameSide) = apply {
        withLoom {
            when (side) {
                GameSide.GLOBAL -> launchConfigs.all { arg(key, value) }
                else -> launchConfigs[side.name.toLowerCase()].arg(key, value)
            }
        }
    }

    /**
     * Makes Loom use a VM
     * property/environment variable.
     */
    fun useProperty(key: String, value: String, side: GameSide) = apply {
        withLoom {
            when (side) {
                GameSide.GLOBAL -> launchConfigs.all { property(key, value) }
                else -> launchConfigs[side.name.toLowerCase()].property(key, value)
            }
        }
    }

    /**
     * Appends a tweaker to your mod.
     */
    fun useTweaker(value: String) = apply {
        val mcData = MCData.from(project)
        useArgument("--tweakClass", value, GameSide.CLIENT)
        project.pluginManager.withPlugin("java") {
            project.tasks.withType<Jar> {
                manifest {
                    val theAttributes = mutableMapOf<String, Any>(
                        "TweakClass" to value
                    )
                    if (mcData.isLegacyForge) theAttributes.apply {
                        this["ForceLoadAsMod"] = true
                        this["TweakOrder"] = "0"
                        this["ModSide"] = "CLIENT"
                    }
                    attributes(theAttributes)
                }
            }
        }
    }

    /**
     * Disables game run configs for
     * the specified game state.
     */
    fun disableRunConfigs(side: GameSide) = apply {
        withLoom {
            when (side) {
                GameSide.GLOBAL -> runConfigs.all { isIdeConfigGenerated = false }
                else -> runConfigs[side.name.toLowerCase()].isIdeConfigGenerated = false
            }
        }
    }

    private fun withLoom(block: LoomGradleExtensionAPI.() -> Unit) {
        project.configure<LoomGradleExtensionAPI> {
            block()
        }
    }
}
