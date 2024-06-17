package dev.deftu.gradle.tools

import dev.deftu.gradle.utils.GitData
import dev.deftu.gradle.utils.MCData
import dev.deftu.gradle.utils.ModData
import dev.deftu.gradle.utils.ProjectData
import gradle.kotlin.dsl.accessors._27ff417d8068e8698d85749e91ce5b3f.bloom

plugins {
    java
    id("dev.deftu.gradle.bloom")
}

val mcData = MCData.from(project)
val modData = ModData.from(project)
val projectData = ProjectData.from(project)
val githubData = GitData.from(project)

bloom {
    if (mcData.isPresent) {
        replacement("@MC_VERSION@", mcData.version)
        replacement("@MOD_LOADER@", mcData.loader.friendlyString)
    }

    if (modData.isPresent) {
        replacement("@MOD_NAME@", modData.name)
        replacement("@MOD_VERSION@", modData.version)
        replacement("@MOD_ID@", modData.id)
    }

    if (projectData.isPresent) {
        replacement("@PROJECT_NAME@", projectData.name)
        replacement("@PROJECT_VERSION@", projectData.version)
        replacement("@PROJECT_GROUP@", projectData.group)
    }

    replacement("@GIT_BRANCH@", githubData.branch)
    replacement("@GIT_COMMIT@", githubData.commit)
    replacement("@GIT_URL@", githubData.url)
}
