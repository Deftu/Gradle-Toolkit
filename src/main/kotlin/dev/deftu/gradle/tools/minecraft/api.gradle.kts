package dev.deftu.gradle.tools.minecraft

plugins {
    id("gg.essential.loom")
}

val extension = extensions.create("toolkitLoomApi", ApiExtension::class)
