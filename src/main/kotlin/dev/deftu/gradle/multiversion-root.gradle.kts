package dev.deftu.gradle

import org.gradle.api.JavaVersion
import dev.deftu.gradle.utils.checkJavaVersion

plugins {
    id("dev.deftu.gradle.preprocess-root")
}

checkJavaVersion(JavaVersion.VERSION_16)
