package dev.deftu.gradle.tools.discord

import gradle.kotlin.dsl.accessors._175fcbe9e04105e3219d6e2795d7218e.implementation
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import xyz.deftu.gradle.DependencyInfo
import xyz.deftu.gradle.utils.shadeOptionally

abstract class JdaExtension {
    abstract val beta: Property<Boolean>

    init {
        beta.convention(false)
    }
}

val extension = extensions.create("toolkitJda", JdaExtension::class.java)

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    shadeOptionally(implementation("net.dv8tion:JDA:${if (extension.beta.getOrElse(false)) DependencyInfo.fetchJdaBetaVersion() else DependencyInfo.fetchJdaVersion()}")!!)
}
