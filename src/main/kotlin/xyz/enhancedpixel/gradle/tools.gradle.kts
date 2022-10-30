package xyz.enhancedpixel.gradle

import xyz.enhancedpixel.gradle.utils.checkGradleVersion
import xyz.enhancedpixel.gradle.utils.checkJavaVersion

// Check if we're in a Java 17 and Gradle 7 environment.
checkJavaVersion(JavaVersion.VERSION_17)
checkGradleVersion(GradleVersion.version("7.0.0"))

// Initialize Github data.
GitHubData.from(project)

// Apply default plugins.
apply(plugin = "xyz.enhancedpixel.gradle.tools.configure")
apply(plugin = "xyz.enhancedpixel.gradle.tools.repo")

pluginManager.withPlugin("java") {
    apply(plugin = "xyz.enhancedpixel.gradle.tools.java")
    apply(plugin = "xyz.enhancedpixel.gradle.tools.resources")
}
pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    apply(plugin = "xyz.enhancedpixel.gradle.tools.kotlin")
}
pluginManager.withPlugin("net.kyori.blossom") {
    apply(plugin = "xyz.enhancedpixel.gradle.tools.blossom")
}
pluginManager.withPlugin("maven-publish") {
    apply(plugin = "xyz.enhancedpixel.gradle.tools.publishing")
}
pluginManager.withPlugin("gg.essential.loom") {
    apply(plugin = "xyz.enhancedpixel.gradle.tools.loom")
}

// Perform our logic.
extensions.create<ToolkitExtension>("enhancedpixel")
