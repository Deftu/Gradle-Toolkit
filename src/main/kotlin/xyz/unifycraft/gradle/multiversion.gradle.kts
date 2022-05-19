package xyz.unifycraft.gradle

import com.replaymod.gradle.preprocess.PreprocessExtension
import com.replaymod.gradle.preprocess.PreprocessPlugin
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.bootstrap.LoomGradlePluginBootstrap

plugins {
    java
}

val mcData = MCData.from(project)

setupLoom()
setupPreprocessor()

fun setupLoom() {
    extra["loom.platform"] = if (mcData.isFabric) "fabric" else "forge"
    apply<LoomGradlePluginBootstrap>()
    extensions.configure<LoomGradleExtensionAPI> {
        runConfigs.all {
            isIdeConfigGenerated = true
        }
    }
}

fun setupPreprocessor() {
    apply<PreprocessPlugin>()
    extensions.configure<PreprocessExtension> {
        vars.put("MC", mcData.version)
        vars.put("FORGE", if (mcData.isForge) 1 else 0)
        vars.put("FABRIC", if (mcData.isFabric) 1 else 0)
    }
}