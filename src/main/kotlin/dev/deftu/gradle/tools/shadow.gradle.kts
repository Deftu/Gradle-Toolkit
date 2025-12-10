package dev.deftu.gradle.tools

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.deftu.gradle.ToolkitConstants
import dev.deftu.gradle.utils.ModData
import org.gradle.jvm.tasks.Jar
import dev.deftu.gradle.utils.withLoom
import dev.deftu.gradle.utils.withLoomPlugin
import gradle.kotlin.dsl.accessors._1169d4f1d0026c4f82c35d8cb5959f57.remapJar

plugins {
    java
}

val shade: Configuration by configurations.creating { }

val fatJar = tasks.register<ShadowJar>("fatJar") {
    group = ToolkitConstants.TASK_GROUP
    description = "Builds a fat JAR with all dependencies shaded in"

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations = mutableListOf(shade) as List<FileCollection>? // Complains about useless cast but also complains about not being a compatible type... Okay...
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("all")

    val javaPlugin = project.extensions.getByType(JavaPluginExtension::class.java)
    val jarTask = project.tasks.getByName("jar") as Jar

    manifest.inheritFrom(jarTask.manifest)
    val libsProvider = project.provider { listOf(jarTask.manifest.attributes["Class-Path"]) }
    val files = project.objects.fileCollection().from(shade)
    doFirst {
        if (!files.isEmpty) {
            val libs = libsProvider.get().toMutableList()
            libs.addAll(files.map { it.name })
            manifest.attributes(mapOf("Class-Path" to libs.filterNotNull().joinToString(" ")))
        }
    }

    from(javaPlugin.sourceSets.getByName("main").output)
    exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")
}

project.artifacts.add("shade", fatJar)

pluginManager.withPlugin("java") {
    tasks["assemble"].dependsOn(fatJar)
}

pluginManager.withLoomPlugin {
    // Set up a non-transitive version of the shade configuration for parity with Loom's `include` configuration.
    // This is mostly only for use with our own `includeOrShade` configuration, which chooses one of the two
    // depending on the mod loader and Minecraft version currently in use. Certain versions may not support
    // Jar-in-Jar which is what `include` uses, and it does NOT shade as a fallback. Thus, meaning our Shadow
    // plugin is still useful. Using them interchangeably with an extended configuration such as `includeOrShade`
    // is useful for that exact reason.
    val shadeNonTransitive by configurations.creating {
        isCanBeConsumed = false
        isCanBeResolved = true
        isTransitive = false
    }

    fatJar.configure {
        configurations.add(shadeNonTransitive)
    }
}

tasks {
    val shadowJar = findByName("shadowJar")
    if (shadowJar != null) {
        named("shadowJar") {
            doFirst {
                throw GradleException("Incorrect task! You're looking for fatJar.")
            }
        }
    }
}

withLoom {
    tasks {
        fatJar {
            archiveClassifier.set("dev")
        }

        remapJar {
            inputFile.set(fatJar.get().archiveFile)
            archiveClassifier.set("")

            val modData = ModData.from(project)
            archiveBaseName.set(modData.name)
        }
    }
}
