package dev.deftu.gradle.utils.version

val MinecraftVersion<*>.patchless: String
    get() = when (this) {
        is MinecraftReleaseVersion -> this.patchless
        else -> this.toString()
    }

val MinecraftVersion<*>.rawVersionString: String
    get() = when (this) {
        is MinecraftReleaseVersion -> this.rawVersion.toString()
        else -> this.toString()
    }
