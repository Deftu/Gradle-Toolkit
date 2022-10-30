package xyz.enhancedpixel.gradle.tools

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import xyz.enhancedpixel.gradle.MCData

val mcData = MCData.from(project)

configure<KotlinJvmProjectExtension> {
    jvmToolchain {
        check(this is JavaToolchainSpec)
        languageVersion.set(JavaLanguageVersion.of(mcData.javaVersion.majorVersion))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = mcData.javaVersion.toString()
    }
}
