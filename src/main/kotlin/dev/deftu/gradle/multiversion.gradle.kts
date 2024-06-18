package dev.deftu.gradle

import com.replaymod.gradle.preprocess.PreprocessExtension
import com.replaymod.gradle.preprocess.PreprocessPlugin
import dev.deftu.gradle.utils.MCData
import net.fabricmc.loom.bootstrap.LoomGradlePluginBootstrap

plugins {
    java
}

val mcData = MCData.from(project)

setupLoom()
setupPreprocessor()

fun setupLoom() {
    extra.set("loom.platform", mcData.loader.friendlyString)
    apply<LoomGradlePluginBootstrap>()
}

fun setupPreprocessor() {
    apply<PreprocessPlugin>()
    extensions.configure<PreprocessExtension> {
        vars.put("MC", mcData.version.rawVersion)
        vars.put("FABRIC", if (mcData.isFabric) 1 else 0)
        vars.put("FORGE-LIKE", if (mcData.isForge || mcData.isNeoForge) 1 else 0)
        vars.put("FORGE", if (mcData.isForge) 1 else 0)
        vars.put("NEOFORGE", if (mcData.isNeoForge) 1 else 0)
    }
}
