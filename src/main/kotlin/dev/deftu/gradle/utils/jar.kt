package dev.deftu.gradle.utils

import net.fabricmc.loom.task.RemapJarTask
import org.gradle.jvm.tasks.Jar

fun Jar.fromRemapJar() {
    val remapJar: RemapJarTask = project.tasks.findByName("remapJar") as? RemapJarTask ?: return
    dependsOn(remapJar)
    from(project.zipTree(remapJar.archiveFile.get()))
}
