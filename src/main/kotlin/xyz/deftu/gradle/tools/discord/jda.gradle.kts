package xyz.deftu.gradle.tools.discord

import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.implementation
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
