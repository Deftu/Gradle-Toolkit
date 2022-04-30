package xyz.unifycraft.gradle.snippets

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.LoomGradlePlugin
import org.gradle.jvm.tasks.Jar
import xyz.unifycraft.gradle.SHADOW_ID

plugins {
    id(SHADOW_ID)
}

configurations.create("unishade")
configurations["implementation"].extendsFrom(configurations["unishade"])

tasks.register("unishadowJar", ShadowJar::class.java) {
    group = "unishadow"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations = listOf(project.configurations["unishade"])

    val javaPlugin = project.convention.getPlugin(JavaPluginConvention::class.java)
    val jarTask = project.tasks.getByName("jar") as Jar

    manifest.inheritFrom(jarTask.manifest)
    val libsProvider = project.provider { listOf(jarTask.manifest.attributes["Class-Path"]) }
    val files = project.objects.fileCollection().from(project.configurations["unishade"])
    doFirst {
        if (!files.isEmpty) {
            val libs = libsProvider.get().toMutableList()
            libs.addAll(files.map { it.name })
            manifest.attributes(mapOf("Class-Path" to libs.filterNotNull().joinToString(" ")))
        }
    }

    from(javaPlugin.sourceSets.getByName("main").output)
    exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")

    project.artifacts.add("unishade", project.tasks.named("unishadowJar"))
}

pluginManager.withPlugin("java") {
    tasks["assemble"].dependsOn(tasks["unishadowJar"])
}

if (plugins.hasPlugin(LoomGradlePlugin::class.java as Class<Plugin<Any>>)) {
    tasks["shadowJar"].doFirst {
        throw GradleException("Incorrect task! You're looking for unishadowJar")
    }

    val remapJar = project.tasks["remapJar"] as Jar
    remapJar.archiveClassifier.set("remapped")
    val unishadowJar = tasks["unishadowJar"] as ShadowJar
    unishadowJar.dependsOn(remapJar)
    unishadowJar.from(remapJar.archiveFile.get())
}