package dev.deftu.gradle

import org.gradle.api.JavaVersion
import xyz.deftu.gradle.utils.checkJavaVersion

plugins {
    id("xyz.deftu.gradle.preprocess-root")
}

checkJavaVersion(JavaVersion.VERSION_16)
