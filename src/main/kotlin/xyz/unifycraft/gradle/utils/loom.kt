package xyz.unifycraft.gradle.utils

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*

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
fun LoomGradleExtensionAPI.useArgument(key: String, value: String, side: GameSide) = apply {
    when (side) {
        GameSide.GLOBAL -> launchConfigs.all { arg(key, value) }
        else -> launchConfigs[side.name.toLowerCase()].arg(key, value)
    }
}

/**
 * Makes Loom use a VM property/environment variable.
 */
fun LoomGradleExtensionAPI.useProperty(key: String, value: String, side: GameSide) = apply {
    when (side) {
        GameSide.GLOBAL -> launchConfigs.all { property(key, value) }
        else -> launchConfigs[side.name.toLowerCase()].property(key, value)
    }
}

// TODO
/**
 * Appends a tweaker to
 * your mod. This is primarily
 * used for legacy Forge modding.
 */
fun Project.useMinecraftTweaker(value: String) = apply {
    pluginManager.withPlugin("gg.essential.loom") {
        configure<LoomGradleExtensionAPI> {
            useArgument("--tweakClass", value, GameSide.CLIENT)
        }
    }

    pluginManager.withPlugin("java") {
        tasks.withType<Jar> {
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
fun LoomGradleExtensionAPI.disableRunConfigs(side: GameSide) = apply {
    when (side) {
        GameSide.GLOBAL -> runConfigs.all { isIdeConfigGenerated = false }
        else -> runConfigs[side.name.toLowerCase()].isIdeConfigGenerated = false
    }
}

