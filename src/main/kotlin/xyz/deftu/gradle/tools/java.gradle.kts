package xyz.deftu.gradle.tools

import org.gradle.api.tasks.compile.JavaCompile
import xyz.deftu.gradle.MCData

plugins {
    java
}

val mcData = MCData.from(project)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

extensions.configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(mcData.javaVersion.majorVersion))
}
