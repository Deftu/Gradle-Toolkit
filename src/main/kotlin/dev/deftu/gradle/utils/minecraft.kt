package dev.deftu.gradle.utils

import net.fabricmc.loom.LoomGradlePlugin
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
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

val JavaVersion.minecraftJavaVersion: String
    get() = when (this) {
        JavaVersion.VERSION_1_8 -> "JAVA_8"
        JavaVersion.VERSION_16 -> "JAVA_16"
        JavaVersion.VERSION_17 -> "JAVA_17"
        JavaVersion.VERSION_21 -> "JAVA_21"
        else -> "UNKNOWN"
    }

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

fun Project.setupLoom(mcData: MCData, action: LoomGradleExtensionAPI.(MCData) -> Unit = {}) {
    extra.set("loom.platform", mcData.loader.friendlyString)
    apply<LoomGradlePlugin>()
    withLoom {
        action(mcData)
    }
}

fun Project.isMultiversionProject(): Boolean {
    fun Project.isVersionFilePresent(): Boolean {
        return file("versions").exists() && File(file("versions"), "mainProject").exists()
    }

    return preprocessorIds.any { id ->
        pluginManager.hasPlugin(id)
    } || rootProject.isVersionFilePresent() || parent?.isVersionFilePresent() == true
}
