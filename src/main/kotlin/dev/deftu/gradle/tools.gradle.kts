package dev.deftu.gradle

import dev.deftu.gradle.utils.GitData
import dev.deftu.gradle.utils.shadeOptionally

// Initialize Git data.
GitData.from(project)

// Apply default plugins.
apply(plugin = "dev.deftu.gradle.tools.configure")
apply(plugin = "dev.deftu.gradle.tools.repo")

// Add default configurations
shadeOptionally // Initialize the shadeOptional configuration.

pluginManager.withPlugin("java") {
    apply(plugin = "dev.deftu.gradle.tools.java")
}

pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    apply(plugin = "dev.deftu.gradle.tools.kotlin")
}
