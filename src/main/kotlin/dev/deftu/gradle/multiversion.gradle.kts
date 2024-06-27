package dev.deftu.gradle

import com.replaymod.gradle.preprocess.PreprocessExtension
import com.replaymod.gradle.preprocess.PreprocessPlugin
import dev.deftu.gradle.utils.MCData
import dev.deftu.gradle.utils.ModLoader
import dev.deftu.gradle.utils.setupLoom

plugins {
    java
}

val mcData = MCData.from(project)

// Set up `loom.platform` and apply Loom
setupLoom(mcData)

// Set up preprocessor
apply<PreprocessPlugin>()
extensions.configure<PreprocessExtension> {
    vars.put("MC", mcData.version.rawVersion)
    vars.put("FABRIC", if (mcData.isFabric) 1 else 0)
    vars.put("FORGE-LIKE", if (mcData.isForge || mcData.isNeoForge) 1 else 0)
    vars.put("FORGE", if (mcData.isForge) 1 else 0)
    vars.put("NEOFORGE", if (mcData.isNeoForge) 1 else 0)
}

// Define root tasks for ease-of-use
val buildTaskName = "buildVersions"
if (rootProject.tasks.none { task ->
    task.name == buildTaskName
}) {
    rootProject.tasks.create(buildTaskName) {
        group = "build"
        description = "Build all versions"

        rootProject.subprojects.map(Project::getName).filter { name ->
            ModLoader.values().any { loader ->
                name.endsWith("-${loader.name}", true)
            }
        }.forEach { version ->
            dependsOn(":$version:build")
        }
    }
}
