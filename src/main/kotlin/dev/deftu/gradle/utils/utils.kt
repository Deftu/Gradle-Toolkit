package dev.deftu.gradle.utils

import dev.deftu.gradle.MCData
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import java.io.File
import kotlin.math.floor

private val loomIds = listOf(
    "fabric-loom",
    "gg.essential.loom",
    "dev.architectury.loom"
)

private val preprocessorIds = listOf(
    "com.replaymod.preprocess-root",
    "com.replaymod.preprocess",
    "com.jab125.preprocessor.preprocess-root",
    "com.jab125.preprocessor.preprocess",
    "dev.deftu.gradle.preprocess",
    "dev.deftu.gradle.preprocess-root",
    "dev.deftu.gradle.multiversion-root",
    "dev.deftu.gradle.multiversion"
)

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

fun Project.getJavaVersionAsInt(): Int {
    val mcData = MCData.from(this)
    return floor(propertyOr("java.version", if (mcData.present) {
        mcData.javaVersion.toString()
    } else JavaVersion.current().toString()).let { version ->
        if (version.startsWith("1.")) {
            version.substring(2)
        } else {
            version
        }.substringBefore(".")
    }.let { version ->
        Regex("[^0-9]").replace(version, "").ifEmpty {
            throw IllegalArgumentException("Invalid java version: $version")
        }
    }.toDouble()).toInt()
}

fun Project.isLoomPresent() = loomIds.any { id ->
    pluginManager.hasPlugin(id)
}

fun Project.withLoom(action: Action<LoomGradleExtensionAPI>) {
    loomIds.forEach { id ->
        pluginManager.withPlugin(id) {
            configure<LoomGradleExtensionAPI> {
                action.execute(this)
            }
        }
    }
}

fun Project.isMultiversionProject(): Boolean = preprocessorIds.any { id ->
    pluginManager.hasPlugin(id)
} || (rootProject.file("versions").exists() && File(rootProject.file("versions"), "mainProject").exists())

fun Project.propertyOr(
    key: String,
    default: String? = null,
    prefix: Boolean = true
): String {
    val key = if (prefix) "dgt.$key" else key
    return (project.findProperty(key)
        ?: System.getProperty(key)
        ?: default) as String?
        ?: throw GradleException("No default property for key \"$key\" found. Set it in gradle.properties, environment variables or in the system properties.")
}

fun Project.getFixedSourcesJarTask() = if (isLoomPresent()) {
    tasks.named<Jar>("remapSourcesJar")
} else {
    tasks.named<Jar>("sourcesJar")
}

fun Project.propertyBoolOr(
    key: String,
    default: Boolean? = null,
    prefix: Boolean = true
) = propertyOr(key, default?.toString(), prefix).toBoolean()

fun Project.propertyIntOr(
    key: String,
    default: Int? = null,
    prefix: Boolean = true
) = propertyOr(key, default?.toString(), prefix).toInt()

fun Project.propertyDoubleOr(
    key: String,
    default: Double? = null,
    prefix: Boolean = true
) = propertyOr(key, default?.toString(), prefix).toDouble()
