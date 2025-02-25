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

private const val DEFAULT_PROPERTY_PREFIX = "dgt."

val Gradle.projectCacheDir: File
    get() = gradle.rootProject.layout.projectDirectory.file(".gradle").asFile

fun Project.getMajorJavaVersion(checkMinecraft: Boolean = true): Int {
    val property = propertyOr("java.version", JavaVersion.current().toString())
    val rawVersion = if (checkMinecraft) {
        val mcData = MCData.from(this)
        if (mcData.isPresent) {
            mcData.version.javaVersion.toString()
        } else property
    } else property

    val formattedVersion = if (rawVersion.startsWith("1.")) {
        rawVersion.substring(2)
    } else {
        rawVersion
    }.substringBefore(".")
    val strippedVersion = Regex("[^0-9]").replace(formattedVersion, "")
    if (strippedVersion.isEmpty()) {
        throw IllegalArgumentException("Invalid java version: $formattedVersion")
    }

    return floor(strippedVersion.toDouble()).toInt()
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
    prefix: String = DEFAULT_PROPERTY_PREFIX
): String {
    val newKey = "$prefix$key"
    return (project.findProperty(newKey)
        ?: System.getProperty(newKey)
        ?: default) as String?
        ?: throw GradleException("No default property for key \"$newKey\" found. Set it in gradle.properties, environment variables or in the system properties.")
}

fun Project.propertyBoolOr(
    key: String,
    default: Boolean? = null,
    prefix: String = DEFAULT_PROPERTY_PREFIX
) = propertyOr(key, default?.toString(), prefix).toBoolean()

fun Project.propertyIntOr(
    key: String,
    default: Int? = null,
    prefix: String = DEFAULT_PROPERTY_PREFIX
) = propertyOr(key, default?.toString(), prefix).toInt()

fun Project.propertyDoubleOr(
    key: String,
    default: Double? = null,
    prefix: String = DEFAULT_PROPERTY_PREFIX
) = propertyOr(key, default?.toString(), prefix).toDouble()
