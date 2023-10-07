package dev.deftu.gradle.tools.discord

import gradle.kotlin.dsl.accessors._175fcbe9e04105e3219d6e2795d7218e.implementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import dev.deftu.gradle.DependencyInfo
import dev.deftu.gradle.utils.shadeOptionally

plugins {
    id("dev.deftu.gradle.tools.discord.kord")
}

repositories {
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    shadeOptionally(implementation("com.kotlindiscord.kord.extensions:kord-extensions:${DependencyInfo.fetchKordExVersion()}")!!)
}
