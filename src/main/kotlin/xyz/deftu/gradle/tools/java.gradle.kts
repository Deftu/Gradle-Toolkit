package xyz.deftu.gradle.tools

import org.gradle.api.tasks.compile.JavaCompile
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

if (mcData.present) {
    tasks.withType<JavaCompile> {
        options.release.set(mcData.javaVersion.toString().let { version ->
            version.substring(0, version.indexOf("."))
        }.toDouble().toInt())
    }

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(mcData.javaVersion.majorVersion))
    }
} else {
    val javaVersion = floor(propertyOr("java.version", JavaVersion.current().toString(), prefix = false).let { version ->
        version.substring(0, version.indexOf("."))
    }.toDouble()).toInt()
    if (javaVersion != 0) {
        tasks.withType<JavaCompile> {
            options.release.set(javaVersion)
        }

        extensions.configure<JavaPluginExtension> {
            toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion.toString()))
        }
    }
}
