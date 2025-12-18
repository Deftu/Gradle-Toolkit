package dev.deftu.gradle.tools.minecraft

plugins {
    id("dev.deftu.gradle.loom")
}

val extension = extensions.create("toolkitLoomApi", ApiExtension::class)
