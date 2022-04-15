package xyz.unifycraft.gradle

import org.gradle.api.JavaVersion
import xyz.unifycraft.gradle.utils.checkJavaVersion

plugins {
    id("com.replaymod.preprocess-root")
}

checkJavaVersion(JavaVersion.VERSION_16)
