package dev.deftu.gradle.tools.discord

import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import dev.deftu.gradle.DependencyInfo
import dev.deftu.gradle.utils.shadeOptionally
import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.implementation

plugins {
    id("dev.deftu.gradle.tools.discord.kord")
}

repositories {
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    shadeOptionally(implementation("com.kotlindiscord.kord.extensions:kord-extensions:${DependencyInfo.fetchKordExVersion()}")!!)
}
