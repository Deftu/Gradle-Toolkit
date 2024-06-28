package dev.deftu.gradle.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

/**
 * Optionally shades a dependency into the outputted JAR file if the shadow plugin is present.
 */
val Project.shadeOptionally: Configuration
    get() = try {
        configurations.getByName("shadeOptional")
    } catch (e: Throwable) {
        val configuration = configurations.create("shadeOptional")
        if (isShadowPluginPresent) {
            configuration.extendsFrom(configurations.getByName("shade"))
        }

        configuration
    }


val Project.includeOrShade: Configuration
    get() = try {
        configurations.getByName("includeOrShade")
    } catch (e: Throwable) {
        if (
            isLoomPluginPresent &&
            isShadowPluginPresent
        ) {
            val mcData = MCData.from(project)
            val configuration = configurations.create("includeOrShade")
            val childConfigName = if (mcData.isFabric) "include" else "shade"
            val childConfig = configurations.findByName(childConfigName)
                ?: throw IllegalStateException("Configuration '$childConfigName' not found!")
            childConfig.extendsFrom(configuration)

            configuration
        } else throw IllegalStateException("${project.name} does not have the required plugins to use includeOrShade (dev.deftu.gradle.tools.minecraft.loom, dev.deftu.gradle.tools.shadow)")
    }
