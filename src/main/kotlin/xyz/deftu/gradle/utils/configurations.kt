package xyz.deftu.gradle.utils

import org.gradle.api.Project

val Project.shadeOptional: org.gradle.api.artifacts.Configuration
    get() = try {
        configurations.getByName("shadeOptional")
    } catch (e: Throwable) {
        val configuration = configurations.create("shadeOptional")
        pluginManager.withPlugin("xyz.deftu.gradle.tools.shadow") {
            configuration.extendsFrom(configurations.getByName("shade"))
        }

        configuration
    }
