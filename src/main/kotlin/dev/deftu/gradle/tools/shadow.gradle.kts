package dev.deftu.gradle.tools

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import gradle.kotlin.dsl.accessors._e453d737b2e0a36dae3d829967a3e2e0.remapJar
import org.gradle.jvm.tasks.Jar
import xyz.deftu.gradle.ModData
import xyz.deftu.gradle.utils.withLoom

plugins {
    java
}

val shade by configurations.creating { }

val fatJar = tasks.register<ShadowJar>("fatJar") {
    group = "deftu"
    description = "Builds a fat JAR with all dependencies shaded in"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations = listOf(shade)
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("all")

    val javaPlugin = project.convention.getPlugin(JavaPluginConvention::class.java)
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
