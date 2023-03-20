package xyz.deftu.gradle.tools

import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.java
import org.gradle.kotlin.dsl.withType
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.utils.propertyOr
import kotlin.math.floor

plugins {
    java
}

val mcData = MCData.from(project)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

fun set(version: Int) {
    if (version > 9) {
        tasks.withType<JavaCompile> {
            options.release.set(version)
        }
    }

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(version))
    }
}

val javaVersion = floor(propertyOr("java.version", if (mcData.present) {
    mcData.javaVersion.toString()
} else JavaVersion.current().toString(), prefix = false).let { version ->
    if (version.startsWith("1.")) {
        version.substring(2)
    } else {
        version
    }.substringBefore(".")
}.toDouble()).toInt()
if (javaVersion != 0) {
    set(javaVersion)
}
