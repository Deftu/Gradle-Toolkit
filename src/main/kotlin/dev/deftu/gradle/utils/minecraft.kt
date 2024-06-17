package dev.deftu.gradle.utils

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File

private val loomIds = listOf(
    "fabric-loom",
    "gg.essential.loom",
    "dev.architectury.loom"
)

private val preprocessorIds = listOf(
    "com.replaymod.preprocess-root",
    "com.replaymod.preprocess",
    "com.jab125.preprocessor.preprocess-root",
    "com.jab125.preprocessor.preprocess",
    "dev.deftu.gradle.preprocess",
    "dev.deftu.gradle.preprocess-root",
    "dev.deftu.gradle.multiversion-root",
    "dev.deftu.gradle.multiversion"
)

fun Project.isLoomPresent() = loomIds.any { id ->
    pluginManager.hasPlugin(id)
}

fun Project.withLoom(action: Action<LoomGradleExtensionAPI>) {
    loomIds.forEach { id ->
        pluginManager.withPlugin(id) {
            configure<LoomGradleExtensionAPI> {
                action.execute(this)
            }
        }
    }
}

fun Project.isMultiversionProject(): Boolean = preprocessorIds.any { id ->
    pluginManager.hasPlugin(id)
} || (rootProject.file("versions").exists() && File(rootProject.file("versions"), "mainProject").exists())
