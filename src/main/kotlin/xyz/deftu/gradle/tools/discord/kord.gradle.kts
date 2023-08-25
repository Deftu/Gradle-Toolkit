package xyz.deftu.gradle.tools.discord

import gradle.kotlin.dsl.accessors._175fcbe9e04105e3219d6e2795d7218e.implementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import xyz.deftu.gradle.DependencyInfo
import xyz.deftu.gradle.utils.shadeOptionally

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    shadeOptionally(implementation("dev.kord:kord-core:${DependencyInfo.fetchKordVersion()}")!!)
}
