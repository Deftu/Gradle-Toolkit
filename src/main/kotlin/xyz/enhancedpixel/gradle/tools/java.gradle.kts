package xyz.enhancedpixel.gradle.tools

import org.gradle.api.tasks.compile.JavaCompile
import xyz.enhancedpixel.gradle.MCData

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
