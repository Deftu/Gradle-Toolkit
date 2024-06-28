package dev.deftu.gradle

import com.replaymod.gradle.preprocess.PreprocessExtension
import com.replaymod.gradle.preprocess.PreprocessPlugin
import dev.deftu.gradle.utils.*

plugins {
    java
}

val mcData = MCData.from(project)
val extension = extensions.create("toolkitMultiversion", MultiVersionExtension::class)

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

// Move builds to the new directory if the extension wants us too
afterEvaluate {
    if (extension.moveBuildsToRootProject.getOrElse(false)) {
        val newBuildDestinationDirectory by lazy {
            rootProject.layout.buildDirectory.asFile.get().resolve("versions")
        }

        fun Jar.moveBuilds() {
            destinationDirectory.set(newBuildDestinationDirectory)
        }

        tasks {
            jar {
                moveBuilds()
            }

            if (isLoomPluginPresent) {
                named<Jar>("remapJar") {
                    moveBuilds()
                }
            }
            
            if (isShadowPluginPresent) {
                named<Jar>("fatJar") {
                    moveBuilds()
                }
            }
        }
    }
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
