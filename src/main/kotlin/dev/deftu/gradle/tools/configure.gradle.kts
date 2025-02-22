package dev.deftu.gradle.tools

import dev.deftu.gradle.utils.*
import gradle.kotlin.dsl.accessors._7f302803de3c8e8ef0ce80f8d318d1c9.base

plugins {
    java
}

val gitData = GitData.from(project)
val mcData = MCData.from(project)
val projectData = ProjectData.from(project)
val modData = ModData.from(project)

fun Project.getFixedVersion(): String {
    val suffix = buildString {
        var content = ""

        val includingGitData = gitData.shouldAppendVersion(project)
        if (includingGitData) {
            content += buildString {
                append(gitData.branch)
                append("-")
                append(gitData.commit)
            }
        }

        if (isMultiversionProject()) {
            content += buildString {
                if (includingGitData) append("+")
                append(mcData.version)
                append("-")
                append(mcData.loader.friendlyString)
            }
        }

        if (content.isNotBlank()) {
            append("+")
            append(content)
        }
    }

    val version = if (modData.isPresent) modData.version else if (projectData.isPresent) projectData.version else project.version
    return "$version$suffix"
}

if (modData.isPresent) {
    if (propertyBoolOr("mod.version.setup", true))
        version = getFixedVersion()
    if (propertyBoolOr("mod.group.setup", true))
        group = modData.group
    if (propertyBoolOr("mod.name.setup", true)) {
        base.archivesName.set(modData.name)
        tasks {
            if (isLoomPresent()) {
                named<org.gradle.jvm.tasks.Jar>("remapJar") {
                    archiveBaseName.set(modData.name)
                }
            } else {
                named<Jar>("jar") {
                    archiveBaseName.set(modData.name)
                }
            }
        }
    }
}

if (projectData.isPresent) {
    if (propertyBoolOr("project.version.setup", true))
        version = getFixedVersion()
    if (propertyBoolOr("project.group.setup", true))
        group = projectData.group
    if (propertyBoolOr("project.name.setup", true)) {
        base.archivesName.set(projectData.name)
        tasks {
            named<Jar>("jar") {
                archiveBaseName.set(projectData.name)
            }
        }
    }
}
