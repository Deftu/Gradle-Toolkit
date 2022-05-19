package xyz.unifycraft.gradle.snippets

import net.kyori.blossom.BlossomExtension
import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.ModData

plugins {
    java
    id("net.kyori.blossom")
}

val mcData = MCData.from(project)
val modData = ModData.from(project)

afterEvaluate {
    configure<BlossomExtension> {
        replaceToken("@MC_VERSION@", mcData.version)
        replaceToken("@MOD_LOADER@", mcData.loader.name)
        replaceToken("@MOD_NAME@", modData.name)
        replaceToken("@MOD_VERSION@", modData.version)
        replaceToken("@MOD_ID@", modData.id)
    }
}
