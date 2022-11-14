package xyz.deftu.gradle.tools

import xyz.deftu.gradle.GitData
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData

plugins {
    java
    id("net.kyori.blossom")
}

val mcData = MCData.from(project)
val modData = ModData.from(project)
val githubData = GitData.from(project)

blossom {
    replaceToken("@MC_VERSION@", mcData.version)
    replaceToken("@MOD_LOADER@", mcData.loader.name)
    replaceToken("@MOD_NAME@", modData.name)
    replaceToken("@MOD_VERSION@", modData.version)
    replaceToken("@MOD_ID@", modData.id)
    replaceToken("@GIT_BRANCH@", githubData.branch)
    replaceToken("@GIT_COMMIT@", githubData.commit)
    replaceToken("@GIT_URL@", githubData.url)
}
