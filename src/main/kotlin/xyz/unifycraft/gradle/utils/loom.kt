package xyz.unifycraft.gradle.utils

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import xyz.unifycraft.gradle.MODGRADLE_ID

/**
 * Specifies a Mixin config
 * for Forge.
 */
@JvmOverloads
fun LoomGradleExtensionAPI.useForgeMixin(namespace: String, file: Boolean = false) {
    forge {
        mixinConfig(if (file) namespace else "mixins.$namespace.json")
    }
}

/**
 * Makes Loom use a command-line
 * argument.
 */
fun LoomGradleExtensionAPI.useArgument(key: String, value: String, state: GameSide) = apply {
    when (state) {
        GameSide.GLOBAL -> launchConfigs.all { arg(key, value) }
        else -> launchConfigs[state.name.toLowerCase()].arg(key, value)
    }
}

/**
 * Appends a tweaker to
 * your mod. This is primarily
 * used for legacy Forge modding.
 */
fun Project.useTweaker(value: String) = apply {
    pluginManager.withPlugin(MODGRADLE_ID) {
        configure<LoomGradleExtensionAPI> {
            useArgument("--tweakClass", value, GameSide.CLIENT)
        }
    }

    pluginManager.withPlugin("java") {
        configure<Jar> {
            manifest {
                attributes(mapOf(
                    "TweakClass" to value
                ))
            }
        }
    }
}

/**
 * Disables game run configs for
 * the specified game state.
 */
fun LoomGradleExtensionAPI.disableRunConfigs(state: GameSide) = apply {
    when (state) {
        GameSide.GLOBAL -> runConfigs.all { isIdeConfigGenerated = false }
        else -> runConfigs[state.name.toLowerCase()].isIdeConfigGenerated = false
    }
}

