package xyz.deftu.gradle.tools.discord

import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.implementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import xyz.deftu.gradle.DependencyInfo
import xyz.deftu.gradle.utils.shadeOptional

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    shadeOptional(implementation("dev.kord:kord-core:${DependencyInfo.fetchKordVersion()}")!!)
}
