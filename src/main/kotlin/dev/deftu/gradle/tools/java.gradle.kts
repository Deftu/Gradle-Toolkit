package dev.deftu.gradle.tools

import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.java
import org.gradle.kotlin.dsl.withType
import dev.deftu.gradle.utils.getMajorJavaVersion
import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.compileJava
import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.java

plugins {
    java
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val version = getMajorJavaVersion()
if (version != 0) {
    val javaVersion = JavaVersion.toVersion(version)

    java {
        targetCompatibility = javaVersion
        sourceCompatibility = javaVersion
    }

    tasks {
        fun applyCompilerOptions(compileOptions: JavaCompile) {
            compileOptions.targetCompatibility = version.toString()
            compileOptions.sourceCompatibility = version.toString()

            if (version > 9) {
                compileOptions.options.release.set(version)
            }
        }

        compileJava {
            applyCompilerOptions(this)
        }

        withType<JavaCompile> {
            applyCompilerOptions(this)
        }
    }

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(version))
    }
}
