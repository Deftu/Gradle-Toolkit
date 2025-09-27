package dev.deftu.gradle.utils.mcinfo

import dev.deftu.gradle.utils.version.MinecraftVersions

class MinecraftInfoV1 : MinecraftInfo() {
    override fun initialize() {
        inherit(MinecraftInfoV0())

        fabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_5 to "0.127.1+1.21.5",
            MinecraftVersions.VERSION_1_21_4 to "0.119.3+1.21.4",
            MinecraftVersions.VERSION_1_21_3 to "0.114.1+1.21.3",
            MinecraftVersions.VERSION_1_21_1 to "0.116.3+1.21.1",
            MinecraftVersions.VERSION_1_20_4 to "0.97.3+1.20.4",
            MinecraftVersions.VERSION_1_20_1 to "0.92.6+1.20.1",
        ))

        legacyFabricYarnVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_13_2 to "1.13.2+build.571:v2",
            MinecraftVersions.VERSION_1_12_2 to "1.12.2+build.571:v2",
            MinecraftVersions.VERSION_1_11_2 to "1.11.2+build.571:v2",
            MinecraftVersions.VERSION_1_10_2 to "1.10.2+build.571:v2",
            MinecraftVersions.VERSION_1_9_4 to "1.9.4+build.571:v2",
            MinecraftVersions.VERSION_1_8_9 to "1.8.9+build.571:v2"
        ))

        legacyFabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_12_2 to "1.12.0+1.12.2",
            MinecraftVersions.VERSION_1_11_2 to "1.12.0+1.11.2",
            MinecraftVersions.VERSION_1_10_2 to "1.12.0+1.10.2",
            MinecraftVersions.VERSION_1_9_4 to "1.12.0+1.9.4",
            MinecraftVersions.VERSION_1_8_9 to "1.12.0+1.8.9"
        ))

        neoForgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_5 to "21.5.79",
            MinecraftVersions.VERSION_1_21_4 to "21.4.140",
            MinecraftVersions.VERSION_1_21_3 to "21.3.79",
            MinecraftVersions.VERSION_1_21_1 to "21.1.182",
            MinecraftVersions.VERSION_1_20_6 to "20.6.135",
            MinecraftVersions.VERSION_1_20_4 to "20.4.248",
            MinecraftVersions.VERSION_1_20_2 to "20.2.93",
        ))
    }
}
