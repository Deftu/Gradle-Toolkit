package dev.deftu.gradle.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.authentication.http.BasicAuthentication
import org.gradle.kotlin.dsl.create

fun Project.getDgtPublishingUsername(): String? {
    val property = project.findProperty("deftu.publishing.username")
    return property?.toString() ?: System.getenv("DEFTU_PUBLISHING_USERNAME")
}

fun Project.getDgtPublishingPassword(): String? {
    val property = project.findProperty("deftu.publishing.password")
    return property?.toString() ?: System.getenv("DEFTU_PUBLISHING_PASSWORD")
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
