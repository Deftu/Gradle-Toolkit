package dev.deftu.gradle

import com.replaymod.gradle.preprocess.PreprocessExtension
import com.replaymod.gradle.preprocess.PreprocessPlugin
import net.fabricmc.loom.bootstrap.LoomGradlePluginBootstrap

plugins {
    java
}

val mcData = MCData.from(project)

setupLoom()
setupPreprocessor()

fun setupLoom() {
    extra.set("loom.platform", if (mcData.isFabric) "fabric" else "forge")
    apply<LoomGradlePluginBootstrap>()
}

fun setupPreprocessor() {
    apply<PreprocessPlugin>()
    extensions.configure<PreprocessExtension> {
        vars.put("MC", mcData.version)
        vars.put("FORGE", if (mcData.isForge) 1 else 0)
        vars.put("FABRIC", if (mcData.isFabric) 1 else 0)
    }
}