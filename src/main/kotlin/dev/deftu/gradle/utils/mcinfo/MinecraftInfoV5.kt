package dev.deftu.gradle.utils.mcinfo

class MinecraftInfoV5 : MinecraftInfo() {
    override fun initialize() {
        inherit(MinecraftInfoV4())

        this.fabricLoaderVersion = "0.18.4"
        this.fabricLanguageKotlinVersion = "1.13.8+kotlin.2.3.0"
    }
}
