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

fun Project.registerMinecraftData(mcData: MCData) = apply {
    if (extensions.findByName("mcData") == null) {
        extensions.add("mcData", mcData)
    }
}