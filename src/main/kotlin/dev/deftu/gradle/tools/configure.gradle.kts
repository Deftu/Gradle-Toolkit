package dev.deftu.gradle.tools

import xyz.deftu.gradle.GitData
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData
import xyz.deftu.gradle.ProjectData
import xyz.deftu.gradle.utils.isLoomPresent
import xyz.deftu.gradle.utils.isMultiversionProject
import xyz.deftu.gradle.utils.propertyBoolOr

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
                append(mcData.versionStr)
                append("-")
                append(mcData.loader.name)
            }
        }

        if (content.isNotBlank()) {
            append("+")
            append(content)
        }
    }

    val version = if (modData.present) modData.version else if (projectData.present) projectData.version else project.version
    return "$version$suffix"
}

if (modData.present) {
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

if (projectData.present) {
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
