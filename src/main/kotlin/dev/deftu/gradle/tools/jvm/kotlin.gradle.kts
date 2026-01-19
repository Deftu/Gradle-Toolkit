package dev.deftu.gradle.tools.jvm

import dev.deftu.gradle.utils.MCData
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import dev.deftu.gradle.utils.getMajorJavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val mcData = MCData.from(project)
if (!mcData.isPresent) {
    val version = getMajorJavaVersion()
    if (version != 0) {
        val javaVersion = JavaVersion.toVersion(version)
        tasks.withType<KotlinCompile> {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(javaVersion.toString()))
            }
        }
    }
} else {
    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(mcData.version.kotlinVersion)
        }
    }
}
