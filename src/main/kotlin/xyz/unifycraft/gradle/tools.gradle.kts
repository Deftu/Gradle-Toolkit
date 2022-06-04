package xyz.unifycraft.gradle

import xyz.unifycraft.gradle.utils.checkGradleVersion
import xyz.unifycraft.gradle.utils.checkJavaVersion

// Check if we're in a Java 17 and Gradle 7 environment.
checkJavaVersion(JavaVersion.VERSION_17)
checkGradleVersion(GradleVersion.version("7.0.0"))

// Initialize Github data.
GithubData.from(project)

// Apply default plugins.
apply(plugin = "xyz.unifycraft.gradle.tools.configure")
apply(plugin = "xyz.unifycraft.gradle.tools.repo")

pluginManager.withPlugin("java") {
    apply(plugin = "xyz.unifycraft.gradle.tools.java")
    apply(plugin = "xyz.unifycraft.gradle.tools.resources")
}
pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    apply(plugin = "xyz.unifycraft.gradle.tools.kotlin")
}
pluginManager.withPlugin("net.kyori.blossom") {
    apply(plugin = "xyz.unifycraft.gradle.tools.blossom")
}
pluginManager.withPlugin("maven-publish") {
    apply(plugin = "xyz.unifycraft.gradle.tools.publishing")
}
pluginManager.withPlugin("gg.essential.loom") {
    apply(plugin = "xyz.unifycraft.gradle.tools.loom")
}

// Perform our logic.
extensions.create<ToolkitExtension>("unifycraft")
