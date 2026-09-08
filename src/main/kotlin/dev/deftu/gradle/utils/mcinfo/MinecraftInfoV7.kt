package dev.deftu.gradle.utils.mcinfo

import dev.deftu.gradle.utils.version.MinecraftVersions

class MinecraftInfoV7 : MinecraftInfo() {
    override fun initialize() {
        inherit(MinecraftInfoV6())

        this.fabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_26_2 to "0.160.0+26.2",
            MinecraftVersions.VERSION_26_1_2 to "0.155.3+26.1.2",
            MinecraftVersions.VERSION_26_1 to "0.145.1+26.1",
        ))

        this.fabricModMenuDefinitions.putAll(listOf(
            MinecraftVersions.VERSION_26_2 to ("com.terraformersmc:modmenu:" to "20.0.1"),
            MinecraftVersions.VERSION_26_1_2 to ("com.terraformersmc:modmenu:" to "18.0.0"),
            MinecraftVersions.VERSION_26_1 to ("com.terraformersmc:modmenu:" to "18.0.0"),
        ))

        this.forgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_26_2 to "26.2-65.1.3",
            MinecraftVersions.VERSION_26_1_2 to "26.1.2-64.1.3",
            MinecraftVersions.VERSION_26_1 to "26.1-62.0.9",
        ))

        this.neoForgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_26_2 to "26.2.0.82",
            MinecraftVersions.VERSION_26_1_2 to "26.1.2.107",
            MinecraftVersions.VERSION_26_1 to "26.1.0.19-beta",
        ))
    }
}
