package dev.deftu.gradle.utils

import org.gradle.api.Project

val Project.shadeOptionally: org.gradle.api.artifacts.Configuration
    get() = try {
        configurations.getByName("shadeOptional")
    } catch (e: Throwable) {
        val configuration = configurations.create("shadeOptional")
        pluginManager.withPlugin("dev.deftu.gradle.tools.shadow") {
            configuration.extendsFrom(configurations.getByName("shade"))
        }

        configuration
    }
