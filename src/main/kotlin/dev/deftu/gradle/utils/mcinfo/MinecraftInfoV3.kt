package dev.deftu.gradle.utils.mcinfo

import dev.deftu.gradle.utils.version.MinecraftVersions

class MinecraftInfoV3 : MinecraftInfo() {
    override fun initialize() {
        inherit(MinecraftInfoV2())

        this.fabricLoaderVersion = "0.17.2"
        this.fabricLanguageKotlinVersion = "1.13.5+kotlin.2.2.10"

        this.fabricYarnVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_10 to "1.21.10+build.1:v2",
            MinecraftVersions.VERSION_1_21_9 to "1.21.9+build.1:v2",
            MinecraftVersions.VERSION_1_21_7 to "1.21.7+build.8:v2",
            MinecraftVersions.VERSION_1_19_2 to "1.19.2+build.28:v2",
        ))

        this.fabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_8 to "0.133.4+1.21.8",
            MinecraftVersions.VERSION_1_21_7 to "0.129.0+1.21.7",
            MinecraftVersions.VERSION_1_21_6 to "0.128.2+1.21.6",
            MinecraftVersions.VERSION_1_21_5 to "0.128.2+1.21.5",
            MinecraftVersions.VERSION_1_21_4 to "0.119.4+1.21.4",
            MinecraftVersions.VERSION_1_21_1 to "0.116.6+1.21.1",

            MinecraftVersions.VERSION_1_18 to "0.44.0+1.18",
        ))

        this.fabricModMenuDefinitions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_8 to ("com.terraformersmc:modmenu:" to "15.0.0"),
            MinecraftVersions.VERSION_1_21_7 to ("com.terraformersmc:modmenu:" to "15.0.0"),
            MinecraftVersions.VERSION_1_21_6 to ("com.terraformersmc:modmenu:" to "15.0.0"),
        ))

        this.legacyFabricYarnVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_13_2 to "1.13.2+build.571:v2",
            MinecraftVersions.VERSION_1_12_2 to "1.12.2+build.571:v2",
            MinecraftVersions.VERSION_1_11_2 to "1.11.2+build.571:v2",
            MinecraftVersions.VERSION_1_10_2 to "1.10.2+build.571:v2",
            MinecraftVersions.VERSION_1_9_4 to "1.9.4+build.571:v2",
            MinecraftVersions.VERSION_1_8_9 to "1.8.9+build.571:v2"
        ))

        this.legacyFabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_12_2 to "1.13.1+1.12.2",
            MinecraftVersions.VERSION_1_11_2 to "1.13.1+1.11.2",
            MinecraftVersions.VERSION_1_10_2 to "1.13.1+1.10.2",
            MinecraftVersions.VERSION_1_9_4 to "1.13.1+1.9.4",
            MinecraftVersions.VERSION_1_8_9 to "1.13.1+1.8.9"
        ))

        this.forgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_20_4 to "1.20.4-49.2.2"
        ))

        this.neoForgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_10 to "21.10.5-beta",
            MinecraftVersions.VERSION_1_21_9 to "21.9.16-beta",
            MinecraftVersions.VERSION_1_21_8 to "21.8.47",
            MinecraftVersions.VERSION_1_21_7 to "21.7.25-beta",
            MinecraftVersions.VERSION_1_21_6 to "21.6.20-beta",
            MinecraftVersions.VERSION_1_21_5 to "21.5.95",
            MinecraftVersions.VERSION_1_21_4 to "21.4.154",
            MinecraftVersions.VERSION_1_21_3 to "21.3.93",
            MinecraftVersions.VERSION_1_21_1 to "21.1.209",

            MinecraftVersions.VERSION_1_20_6 to "20.6.138",
            MinecraftVersions.VERSION_1_20_4 to "20.4.250",
            MinecraftVersions.VERSION_1_20_2 to "20.2.93"
        ))
    }
}
