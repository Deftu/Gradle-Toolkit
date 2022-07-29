package xyz.unifycraft.gradle.utils

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.util.GradleVersion
import xyz.unifycraft.gradle.MCData

/**
 * Taken from Essential under GPL 3.0
 * https://github.com/EssentialGG/essential-gradle-toolkit/blob/master/LICENSE.md
 */
fun checkJavaVersion(minVersion: JavaVersion) {
    if (JavaVersion.current() < minVersion) {
        throw GradleException(listOf(
            "Java $minVersion is required to build (running ${JavaVersion.current()}).",
            if (System.getProperty("idea.active").toBoolean()) {
                "In IDEA: Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Gradle JVM"
            } else {
                "Current JAVA_HOME: ${System.getenv("JAVA_HOME")}"
            },
        ).joinToString("\n"))
    }
}

fun checkGradleVersion(minVersion: GradleVersion) {
    if (GradleVersion.current() < minVersion) {
        throw GradleException("Gradle $minVersion is required to build (running ${GradleVersion.current()}).")
    }
}

fun Project.isLoomPresent(): Boolean =
    (pluginManager.hasPlugin("fabric-loom") || pluginManager.hasPlugin("gg.essential.loom") || pluginManager.hasPlugin("dev.architectury.loom"))
fun Project.isMultiversionProject(): Boolean =
    (pluginManager.hasPlugin("com.replaymod.preprocess-root") || pluginManager.hasPlugin("com.replaymod.preprocess") || pluginManager.hasPlugin("xyz.unifycraft.gradle.multiversion-root") || pluginManager.hasPlugin("xyz.unifycraft.gradle.multiversion"))

fun Project.propertyOr(key: String, default: String? = null) =
    (project.findProperty(key)
        ?: System.getProperty(key)
        ?: default
        ?: throw GradleException("No default property for key \"$key\" found. Set it in gradle.properties, environment variables or in the system properties.")) as String?

fun Project.propertyBoolOr(key: String, default: Boolean = false) =
    propertyOr(key, default.toString()).toBoolean()
