package dev.deftu.gradle.tools

import dev.deftu.gradle.utils.*
import gradle.kotlin.dsl.accessors._7f302803de3c8e8ef0ce80f8d318d1c9.base

val gitData = GitData.from(project)
val mcData = MCData.from(project)
val projectData = ProjectData.from(project)
val modData = ModData.from(project)

if (projectData.isPresent) {
    applyProjectInfo(projectData, "project") {
        pluginManager.withPlugin("java") {
            tasks {
                named<Jar>("jar") {
                    archiveBaseName.set(projectData.name)
                }
            }
        }
    }
}

if (modData.isPresent) {
    applyProjectInfo(modData, "mod") {
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

val Project.trueVersion: String
    get() {
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

fun applyProjectInfo(info: ProjectInfo, prefix: String, setupBlock: () -> Unit) {
    if (propertyBoolOr("$prefix.version.setup", true)) {
        version = trueVersion
    }

    if (propertyBoolOr("$prefix.group.setup", true)) {
        group = info.group
    }

    if (propertyBoolOr("$prefix.name.setup", true)) {
        pluginManager.withPlugin("base") {
            base.archivesName.set(info.name)
        }

        setupBlock.invoke()
    }
}
