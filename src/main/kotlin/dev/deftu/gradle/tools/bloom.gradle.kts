package dev.deftu.gradle.tools

import dev.deftu.gradle.GitData
import dev.deftu.gradle.MCData
import dev.deftu.gradle.ModData
import dev.deftu.gradle.ProjectData

plugins {
    java
    id("dev.deftu.gradle.bloom")
}

val mcData = MCData.from(project)
val modData = ModData.from(project)
val projectData = ProjectData.from(project)
val githubData = GitData.from(project)

bloom {
    if (mcData.present) {
        replacement("@MC_VERSION@", mcData.version)
        replacement("@MOD_LOADER@", mcData.loader.name)
    }

    if (modData.present) {
        replacement("@MOD_NAME@", modData.name)
        replacement("@MOD_VERSION@", modData.version)
        replacement("@MOD_ID@", modData.id)
    }

    if (projectData.present) {
        replacement("@PROJECT_NAME@", projectData.name)
        replacement("@PROJECT_VERSION@", projectData.version)
        replacement("@PROJECT_GROUP@", projectData.group)
    }

    replacement("@GIT_BRANCH@", githubData.branch)
    replacement("@GIT_COMMIT@", githubData.commit)
    replacement("@GIT_URL@", githubData.url)
}
