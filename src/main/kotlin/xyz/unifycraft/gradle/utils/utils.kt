package xyz.unifycraft.gradle.utils

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
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

fun Project.propertyOr(key: String, default: String? = null) =
    (findProperty(key)
        ?: System.getProperty(key)
        ?: default
        ?: throw GradleException("No default property for key \"$key\" found. Set it in gradle.properties, environment variables or in the system properties.")) as String?

fun Project.propertyBoolOr(key: String, default: Boolean = false) =
    propertyOr(key, default.toString()).toBoolean()
