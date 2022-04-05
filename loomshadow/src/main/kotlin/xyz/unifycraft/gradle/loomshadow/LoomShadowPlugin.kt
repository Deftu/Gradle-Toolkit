package xyz.unifycraft.gradle.loomshadow

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.InvalidPluginException
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.jvm.tasks.Jar
import xyz.unifycraft.gradle.base.BasePlugin

class LoomShadowPlugin : BasePlugin() {
    override fun onApply(project: Project) {
        if (!project.plugins.hasPlugin("com.github.johnrengelman.shadow"))
            throw InvalidPluginException("LoomShadowPlugin requires the Shadow plugin to be present.")
        val configuration = project.configurations.maybeCreate(CONFIGURATION)
        project.configurations.getByName("implementation").extendsFrom(configuration)
        val loomShadowJar = project.tasks.register(TASK, ShadowJar::class.java) {
            configureTask(project, it)
        }.get()
        project.tasks.getByName("assemble").dependsOn(loomShadowJar)
        project.afterEvaluate {
            if (
                project.plugins.hasPlugin("net.fabricmc.loom") ||
                project.plugins.hasPlugin("dev.architectury.loom") ||
                project.plugins.hasPlugin("gg.essential.loom")
            ) {
                project.tasks.getByName("shadowJar").doFirst {
                    throw GradleException("Incorrect task! You're looking for loomShadowJar")
                }

                val remapJar = project.tasks.getByName("remapJar") as Jar
                remapJar.archiveClassifier.set("remapped")
                loomShadowJar.dependsOn(remapJar)
                loomShadowJar.from(remapJar.archiveFile.get())
            }
        }
    }

    private fun configureTask(project: Project, task: ShadowJar) {
        val configuration = project.configurations.getByName(CONFIGURATION)
        task.group = "loom shadow"
        task.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        task.configurations = listOf(configuration)

        val javaPlugin = project.convention.getPlugin(JavaPluginConvention::class.java)
        val jarTask = project.tasks.getByName("jar") as Jar

        task.manifest.inheritFrom(jarTask.manifest)
        val libsProvider = project.provider { listOf(jarTask.manifest.attributes["Class-Path"]) }
        val files = project.objects.fileCollection().from(configuration)
        task.doFirst {
            if (!files.isEmpty) {
                val libs = libsProvider.get().toMutableList()
                libs.addAll(files.map { it.name })
                task.manifest.attributes(mapOf("Class-Path" to libs.filterNotNull().joinToString(" ")))
            }
        }

        task.from(javaPlugin.sourceSets.getByName("main").output)
        task.exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")

        project.artifacts.add("loomShade", project.tasks.named("loomShadowJar"))
    }

    companion object {
        const val TASK = "loomShadowJar"
        const val CONFIGURATION = "loomShade"
    }
}