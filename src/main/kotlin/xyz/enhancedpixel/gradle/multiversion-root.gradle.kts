package xyz.enhancedpixel.gradle

import org.gradle.api.JavaVersion
import xyz.enhancedpixel.gradle.utils.checkJavaVersion

plugins {
    id("com.replaymod.preprocess-root")
}

checkJavaVersion(JavaVersion.VERSION_16)
