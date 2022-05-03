package xyz.unifycraft.gradle.utils

import org.gradle.api.GradleException
import org.gradle.api.Project

fun Project.propertyOr(namespace: String = "tools", key: String, default: String? = null) =
    (findProperty("xyz.unifycraft.$namespace.$key")
        ?: System.getProperty("xyz.unifycraft.$namespace.$key")
        ?: default
        ?: throw GradleException("No default property for key \"xyz.unifycraft.$namespace.$key\" found. Set it in gradle.properties, environment variables or in the system properties.")) as String