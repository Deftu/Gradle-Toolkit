package xyz.unifycraft.gradle.utils

import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.extension.LoomGradleExtensionApiImpl
import net.fabricmc.loom.extension.LoomGradleExtensionImpl
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import xyz.unifycraft.gradle.MCData

// Internal

private val LoomGradleExtensionImpl.theProject: Project
    get() = javaClass.getDeclaredField("project").let {
        it.isAccessible = true
        it.get(this) as Project
    }

// Exposed

/**
 * Specifies a Mixin config
 * for Forge.
 */
@JvmOverloads
fun LoomGradleExtensionImpl.useForgeMixin(namespace: String, file: Boolean = false) {
    val value = if (file) namespace else "mixins.$namespace.json"
    forge {
        mixinConfig(value)
    }
    theProject.pluginManager.withPlugin("java") {
        theProject.tasks.withType<Jar> {
            manifest {
                attributes(mapOf(
                    "MixinConfigs" to value
                ))
            }
        }
    }
}

/**
 * Makes Loom use a command-line
 * argument.
 */
fun LoomGradleExtensionImpl.useArgument(key: String, value: String, side: GameSide) = apply {
    when (side) {
        GameSide.GLOBAL -> launchConfigs.all { arg(key, value) }
        else -> launchConfigs[side.name.toLowerCase()].arg(key, value)
    }
}

/**
 * Makes Loom use a VM property/environment variable.
 */
fun LoomGradleExtensionImpl.useProperty(key: String, value: String, side: GameSide) = apply {
    when (side) {
        GameSide.GLOBAL -> launchConfigs.all { property(key, value) }
        else -> launchConfigs[side.name.toLowerCase()].property(key, value)
    }
}

/**
 * Appends a tweaker to
 * your mod.
 *
 * **Legacy Forge**
 */
fun LoomGradleExtensionImpl.useTweaker(value: String) = apply {
    val mcData = MCData.from(theProject)
    useArgument("--tweakClass", value, GameSide.CLIENT)
    theProject.pluginManager.withPlugin("java") {
        theProject.tasks.withType<Jar> {
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
fun LoomGradleExtensionImpl.disableRunConfigs(side: GameSide) = apply {
    when (side) {
        GameSide.GLOBAL -> runConfigs.all { isIdeConfigGenerated = false }
        else -> runConfigs[side.name.toLowerCase()].isIdeConfigGenerated = false
    }
}

