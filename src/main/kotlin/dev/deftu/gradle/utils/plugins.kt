package dev.deftu.gradle.utils

import dev.deftu.gradle.tools.ShadowPlugin
import dev.deftu.gradle.tools.minecraft.LoomPlugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager

val Project.isLoomPluginPresent: Boolean
    get() = plugins.hasPlugin(LoomPlugin::class.java)

val Project.isShadowPluginPresent: Boolean
    get() = plugins.hasPlugin(ShadowPlugin::class.java)

fun PluginManager.withLoomPlugin(block: () -> Unit) {
    withPlugin("dev.deftu.gradle.tools.minecraft.loom") {
        block()
    }
}

fun PluginManager.withShadowPlugin(block: () -> Unit) {
    withPlugin("dev.deftu.gradle.tools.ShadowPlugin") {
        block()
    }
}
