package xyz.unifycraft.gradle

apply(plugin = "xyz.unifycraft.gradle.snippets.repo")

pluginManager.withPlugin("java") {
    apply(plugin = "xyz.unifycraft.gradle.snippets.java")
    apply(plugin = "xyz.unifycraft.gradle.snippets.resources")
}
pluginManager.withPlugin("xyz.unifycraft.gradle.loom") {
    apply(plugin = "xyz.unifycraft.gradle.snippets.loom")
}
