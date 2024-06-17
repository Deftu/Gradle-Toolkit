package dev.deftu.gradle.utils

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.invocation.Gradle
import org.gradle.authentication.http.BasicAuthentication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.named
import java.io.File
import kotlin.math.floor

val Gradle.projectCacheDir: File
    get() = gradle.rootProject.layout.projectDirectory.file(".gradle").asFile

fun Project.getMajorJavaVersion(): Int {
    val mcData = MCData.from(this)
    return floor(propertyOr("java.version", if (mcData.isPresent) {
        mcData.version.javaVersion.toString()
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

fun Project.getSourcesJarTask() =
    if (isLoomPresent()) {
        tasks.named<Jar>("remapSourcesJar")
    } else {
        tasks.named<Jar>("sourcesJar")
    }

fun MavenArtifactRepository.applyBasicCredentials(
    username: String,
    password: String
) {
    authentication.create<BasicAuthentication>("basic")
    credentials {
        this.username = username
        this.password = password
    }
}

fun Project.propertyOr(
    key: String,
    default: String? = null,
    prefix: Boolean = true
): String {
    val newKey = if (prefix) "dgt.$key" else key
    return (project.findProperty(newKey)
        ?: System.getProperty(newKey)
        ?: default) as String?
        ?: throw GradleException("No default property for key \"$newKey\" found. Set it in gradle.properties, environment variables or in the system properties.")
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
