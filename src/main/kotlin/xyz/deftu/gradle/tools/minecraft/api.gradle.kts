package xyz.deftu.gradle.tools.minecraft

plugins {
    id("gg.essential.loom")
}

val extension = extensions.create("loomApi", ApiExtension::class)
