package dev.deftu.gradle.tools

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import dev.deftu.gradle.utils.getJavaVersionAsInt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val version = getJavaVersionAsInt()
if (version != 0) {
    val javaVersion = JavaVersion.toVersion(version)
    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(javaVersion.toString()))
        }
    }

    configure<KotlinJvmProjectExtension> {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(version))
        }
    }
}
