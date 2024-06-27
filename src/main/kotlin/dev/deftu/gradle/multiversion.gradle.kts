package dev.deftu.gradle

import com.replaymod.gradle.preprocess.PreprocessExtension
import com.replaymod.gradle.preprocess.PreprocessPlugin
import dev.deftu.gradle.utils.MCData
import dev.deftu.gradle.utils.setupLoom

plugins {
    java
}

val mcData = MCData.from(project)

setupLoom(mcData)
setupPreprocessor()

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
