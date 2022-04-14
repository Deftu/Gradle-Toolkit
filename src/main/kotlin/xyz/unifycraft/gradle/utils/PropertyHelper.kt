package xyz.unifycraft.gradle.utils

import org.gradle.api.GradleException
import org.gradle.api.Project

fun Project.propertyOr(key: String, default: String?) =
    (findProperty("xyz.unifycraft.gradle.loomconfig.$key")
        ?: System.getProperty(key)
        ?: default
        ?: throw GradleException("No default property for key $key found. Set it in gradle.properties, environment variables or in the system properties.")) as String