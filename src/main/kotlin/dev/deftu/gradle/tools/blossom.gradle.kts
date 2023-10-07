package dev.deftu.gradle.tools

import xyz.deftu.gradle.GitData
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData
import xyz.deftu.gradle.ProjectData

plugins {
    java
    id("net.kyori.blossom")
}

val mcData = MCData.from(project)
val modData = ModData.from(project)
val projectData = ProjectData.from(project)
val githubData = GitData.from(project)

blossom {
    if (mcData.present) {
        replaceToken("@MC_VERSION@", mcData.version)
        replaceToken("@MOD_LOADER@", mcData.loader.name)
    }

    if (modData.present) {
        replaceToken("@MOD_NAME@", modData.name)
        replaceToken("@MOD_VERSION@", modData.version)
        replaceToken("@MOD_ID@", modData.id)
    }

    if (projectData.present) {
        replaceToken("@PROJECT_NAME@", projectData.name)
        replaceToken("@PROJECT_VERSION@", projectData.version)
        replaceToken("@PROJECT_GROUP@", projectData.group)
    }

    replaceToken("@GIT_BRANCH@", githubData.branch)
    replaceToken("@GIT_COMMIT@", githubData.commit)
    replaceToken("@GIT_URL@", githubData.url)
}
