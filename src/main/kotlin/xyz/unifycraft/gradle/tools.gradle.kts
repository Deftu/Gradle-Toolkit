package xyz.unifycraft.gradle

apply(plugin = "xyz.unifycraft.gradle.snippets.configure")
apply(plugin = "xyz.unifycraft.gradle.snippets.repo")

pluginManager.withPlugin("java") {
    apply(plugin = "xyz.unifycraft.gradle.snippets.java")
    apply(plugin = "xyz.unifycraft.gradle.snippets.resources")
}
pluginManager.withPlugin("net.kyori.blossom") {
    apply(plugin = "xyz.unifycraft.gradle.snippets.blossom")
}
pluginManager.withPlugin("maven-publish") {
    apply(plugin = "xyz.unifycraft.gradle.snippets.publishing")
}
pluginManager.withPlugin("gg.essential.loom") {
    apply(plugin = "xyz.unifycraft.gradle.snippets.loom")
}
