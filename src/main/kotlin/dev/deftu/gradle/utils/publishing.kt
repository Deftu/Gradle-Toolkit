package dev.deftu.gradle.utils

import org.gradle.api.Project

fun Project.getDgtPublishingUsername(): String? {
    val property = project.findProperty("deftu.publishing.username")
    return property?.toString() ?: System.getenv("DEFTU_PUBLISHING_USERNAME")
}

fun Project.getDgtPublishingPassword(): String? {
    val property = project.findProperty("deftu.publishing.password")
    return property?.toString() ?: System.getenv("DEFTU_PUBLISHING_PASSWORD")
}
