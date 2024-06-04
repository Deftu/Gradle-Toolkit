package dev.deftu.gradle.tools

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import dev.deftu.gradle.utils.getJavaVersionAsInt

val version = getJavaVersionAsInt()
if (version != 0) {
    val javaVersion = JavaVersion.toVersion(version)
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaVersion.toString()
        }
    }

    configure<KotlinJvmProjectExtension> {
        jvmToolchain {
            check(this is JavaToolchainSpec)
            languageVersion.set(JavaLanguageVersion.of(version))
        }
    }
}
