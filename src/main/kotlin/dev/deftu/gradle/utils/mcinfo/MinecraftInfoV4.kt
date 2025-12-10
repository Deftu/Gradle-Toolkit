package dev.deftu.gradle.utils.mcinfo

import dev.deftu.gradle.utils.version.MinecraftVersions

class MinecraftInfoV4 : MinecraftInfo() {
    override fun initialize() {
        inherit(MinecraftInfoV3())

        this.fabricLoaderVersion = "0.18.2"
        this.fabricLanguageKotlinVersion = "1.13.7+kotlin.2.2.21"

        this.fabricYarnVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "1.21.11+build.2:v2",
        ))

        this.fabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "0.139.4+1.21.11",
            MinecraftVersions.VERSION_1_21_10 to "0.138.3+1.21.10",
            MinecraftVersions.VERSION_1_21_9 to "0.134.0+1.21.9",
            MinecraftVersions.VERSION_1_21_8 to "0.136.1+1.21.8",
        ))

        this.kotlinForForgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_20_6 to "5.10.0",
        ))
    }
}
