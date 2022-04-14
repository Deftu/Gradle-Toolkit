package xyz.unifycraft.gradle

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.LoomGradlePlugin
import org.gradle.jvm.tasks.Jar

plugins {
    id("com.github.johnrengelman.shadow")
}

val extension = extensions.create("loomShadow", LoomShadowExtension::class.java)

configurations.create("loomShade")
configurations["implementation"].extendsFrom(configurations["loomShade"])

tasks.register("loomShadowJar", ShadowJar::class.java) {
    group = "loomshadow"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations = listOf(project.configurations["loomShade"])

    val javaPlugin = project.convention.getPlugin(JavaPluginConvention::class.java)
    val jarTask = project.tasks.getByName("jar") as Jar

    manifest.inheritFrom(jarTask.manifest)
    val libsProvider = project.provider { listOf(jarTask.manifest.attributes["Class-Path"]) }
    val files = project.objects.fileCollection().from(project.configurations["loomShade"])
    doFirst {
        if (!files.isEmpty) {
            val libs = libsProvider.get().toMutableList()
            libs.addAll(files.map { it.name })
            manifest.attributes(mapOf("Class-Path" to libs.filterNotNull().joinToString(" ")))
        }
    }

    from(javaPlugin.sourceSets.getByName("main").output)
    exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")

    project.artifacts.add("loomShade", project.tasks.named("loomShadowJar"))
}
if (extension.autoDepend.getOrElse(false))
    tasks["assemble"].dependsOn(tasks["loomShadowJar"])

if (plugins.hasPlugin(LoomGradlePlugin::class.java as Class<Plugin<Any>>)) {
    tasks["shadowJar"].doFirst {
        throw GradleException("Incorrect task! You're looking for loomShadowJar")
    }

    val remapJar = project.tasks["remapJar"] as Jar
    remapJar.archiveClassifier.set("remapped")
    val loomShadowJar = tasks["loomShadowJar"] as ShadowJar
    loomShadowJar.dependsOn(remapJar)
    loomShadowJar.from(remapJar.archiveFile.get())
}