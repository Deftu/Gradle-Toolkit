package xyz.deftu.gradle.tools

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.utils.propertyOr
import kotlin.math.floor

val mcData = MCData.from(project)

fun set(version: Int) {
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
