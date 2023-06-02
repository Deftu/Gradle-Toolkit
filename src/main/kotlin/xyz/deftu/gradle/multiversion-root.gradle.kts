package xyz.deftu.gradle

import org.gradle.api.JavaVersion
import xyz.deftu.gradle.utils.checkJavaVersion

plugins {
    id("com.jab125.preprocessor.preprocess-root")
}

checkJavaVersion(JavaVersion.VERSION_16)
