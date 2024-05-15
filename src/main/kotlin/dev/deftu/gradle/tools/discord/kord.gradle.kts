package dev.deftu.gradle.tools.discord

import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import dev.deftu.gradle.DependencyInfo
import dev.deftu.gradle.utils.shadeOptionally
import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.implementation

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    shadeOptionally(implementation("dev.kord:kord-core:${DependencyInfo.fetchKordVersion()}")!!)
}
