package xyz.deftu.gradle

import xyz.deftu.gradle.utils.checkJavaVersion
import xyz.deftu.gradle.utils.shadeOptionally
import xyz.deftu.gradle.utils.withLoom

// Check if we're in a Java 17 environment.
checkJavaVersion(JavaVersion.VERSION_17)

// Initialize Git data.
GitData.from(project)

// Apply default plugins.
apply(plugin = "xyz.deftu.gradle.tools.configure")
apply(plugin = "xyz.deftu.gradle.tools.repo")

// Add default configurations
shadeOptionally // Initialize the shadeOptional configuration.

pluginManager.withPlugin("java") {
    apply(plugin = "xyz.deftu.gradle.tools.java")
    apply(plugin = "xyz.deftu.gradle.tools.resources")
}

pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    apply(plugin = "xyz.deftu.gradle.tools.kotlin")
    apply(plugin = "xyz.deftu.gradle.tools.resources")
}

// Perform our logic.
extensions.create<ToolkitExtension>("toolkit")
