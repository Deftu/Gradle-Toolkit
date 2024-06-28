package dev.deftu.gradle.utils

import dev.deftu.gradle.tools.ShadowPlugin
import dev.deftu.gradle.tools.minecraft.LoomPlugin
import org.gradle.api.Project

val Project.isLoomPluginPresent: Boolean
    get() = plugins.hasPlugin(LoomPlugin::class.java)

val Project.isShadowPluginPresent: Boolean
    get() = plugins.hasPlugin(ShadowPlugin::class.java)
