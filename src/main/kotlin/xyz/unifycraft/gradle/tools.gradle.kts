package xyz.unifycraft.gradle

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
