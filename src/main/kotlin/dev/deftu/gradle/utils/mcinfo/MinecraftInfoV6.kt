package dev.deftu.gradle.utils.mcinfo

import dev.deftu.gradle.utils.version.MinecraftVersions

class MinecraftInfoV6 : MinecraftInfo() {
    override fun initialize() {
        inherit(MinecraftInfoV5())

        this.fabricLoaderVersion = "0.19.5"
        this.fabricLanguageKotlinVersion = "1.13.11+kotlin.2.3.21"

        this.fabricYarnVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "1.21.11+build.6:v2",
            MinecraftVersions.VERSION_1_21_10 to "1.21.10+build.3:v2",
        ))

        this.fabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "0.141.6+1.21.11",
            MinecraftVersions.VERSION_1_21_10 to "0.138.4+1.21.10",
            MinecraftVersions.VERSION_1_21_9 to "0.134.1+1.21.9",
        ))

        this.fabricModMenuDefinitions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to ("com.terraformersmc:modmenu:" to "17.0.0"),
            MinecraftVersions.VERSION_1_21_10 to ("com.terraformersmc:modmenu:" to "16.0.1"),
            MinecraftVersions.VERSION_1_21_9 to ("com.terraformersmc:modmenu:" to "16.0.1"),
        ))

        this.legacyFabricYarnVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_13_2 to "1.13.2+build.604:v2",
            MinecraftVersions.VERSION_1_12_2 to "1.12.2+build.604:v2",
            MinecraftVersions.VERSION_1_11_2 to "1.11.2+build.604:v2",
            MinecraftVersions.VERSION_1_10_2 to "1.10.2+build.604:v2",
            MinecraftVersions.VERSION_1_9_4 to "1.9.4+build.604:v2",
            MinecraftVersions.VERSION_1_8_9 to "1.8.9+build.604:v2"
        ))

        this.legacyFabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_12_2 to "1.13.5+1.12.2",
            MinecraftVersions.VERSION_1_11_2 to "1.13.5+1.11.2",
            MinecraftVersions.VERSION_1_10_2 to "1.13.5+1.10.2",
            MinecraftVersions.VERSION_1_9_4 to "1.13.5+1.9.4",
            MinecraftVersions.VERSION_1_8_9 to "1.13.5+1.8.9"
        ))

        this.featherVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_14_4 to "1.14.4+build.2",
            MinecraftVersions.VERSION_1_14_3 to "1.14.3+build.2",
            MinecraftVersions.VERSION_1_14_2 to "1.14.2+build.2",
            MinecraftVersions.VERSION_1_14_1 to "1.14.1+build.2",
            MinecraftVersions.VERSION_1_14 to "1.14+build.2",

            MinecraftVersions.VERSION_1_13_2 to "1.13.2+build.2",
            MinecraftVersions.VERSION_1_13_1 to "1.13.1+build.2",
            MinecraftVersions.VERSION_1_13 to "1.13+build.2",

            MinecraftVersions.VERSION_1_12_2 to "1.12.2+build.2",
            MinecraftVersions.VERSION_1_12_1 to "1.12.1+build.2",
            MinecraftVersions.VERSION_1_12 to "1.12+build.2",

            MinecraftVersions.VERSION_1_11_2 to "1.11.2+build.2",
            MinecraftVersions.VERSION_1_11_1 to "1.11.1+build.2",
            MinecraftVersions.VERSION_1_11 to "1.11+build.2",

            MinecraftVersions.VERSION_1_10_2 to "1.10.2+build.2",
            MinecraftVersions.VERSION_1_10_1 to "1.10.1+build.2",
            MinecraftVersions.VERSION_1_10 to "1.10+build.2",

            MinecraftVersions.VERSION_1_9_4 to "1.9.4+build.2",
            MinecraftVersions.VERSION_1_9_3 to "1.9.3+build.2",
            MinecraftVersions.VERSION_1_9_2 to "1.9.2+build.2",
            MinecraftVersions.VERSION_1_9_1 to "1.9.1+build.2",
            MinecraftVersions.VERSION_1_9 to "1.9+build.2",

            MinecraftVersions.VERSION_1_8_9 to "1.8.9+build.2"
        ))

        this.legacyYarnVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_13_2 to "1.13.2+build.4",
            MinecraftVersions.VERSION_1_12_2 to "1.12.2+build.4",
            MinecraftVersions.VERSION_1_11_2 to "1.11.2+build.4",
            MinecraftVersions.VERSION_1_10_2 to "1.10.2+build.4",
            MinecraftVersions.VERSION_1_9_4 to "1.9.4+build.4",
            MinecraftVersions.VERSION_1_8_9 to "1.8.9+build.4"
        ))

        this.forgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "1.21.11-61.2.1",
            MinecraftVersions.VERSION_1_21_10 to "1.21.10-60.1.15",
        ))

        this.neoForgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "21.11.45",
            MinecraftVersions.VERSION_1_21_10 to "21.10.64",
        ))
    }
}
