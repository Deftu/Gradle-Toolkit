package xyz.deftu.gradle.tools.discord

import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.implementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import xyz.deftu.gradle.DependencyInfo
import xyz.deftu.gradle.utils.shadeOptional

plugins {
    id("xyz.deftu.gradle.tools.discord.kord")
}

repositories {
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    shadeOptional(implementation("com.kotlindiscord.kord.extensions:kord-extensions:${DependencyInfo.fetchKordExVersion()}")!!)
}
