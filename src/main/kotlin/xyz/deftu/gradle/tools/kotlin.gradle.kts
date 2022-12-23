package xyz.deftu.gradle.tools

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.utils.propertyOr
import kotlin.math.floor

val mcData = MCData.from(project)

configure<KotlinJvmProjectExtension> {
    jvmToolchain {
        check(this is JavaToolchainSpec)
        languageVersion.set(JavaLanguageVersion.of(mcData.javaVersion.majorVersion))
    }
}

if (mcData.present) {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = mcData.javaVersion.majorVersion
        }
    }
} else {
    val javaVersion = floor(propertyOr("java.version", JavaVersion.current().toString(), prefix = false).let { version ->
        if (version.startsWith("1.")) version.substring(2) else version
    }.toDouble()).toInt()
    if (javaVersion != 0) {
        tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = javaVersion.toString()
            }
        }
    }
}
