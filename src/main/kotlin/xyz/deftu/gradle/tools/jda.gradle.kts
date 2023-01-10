package xyz.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.implementation
import org.gradle.api.provider.Property
import xyz.deftu.gradle.DependencyInfo
import xyz.deftu.gradle.utils.shadeOptional

abstract class JdaExtension {
    abstract val beta: Property<Boolean>

    init {
        beta.convention(false)
    }
}

val extension = extensions.create("jda", JdaExtension::class.java)

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    shadeOptional(implementation("net.dv8tion:JDA:${if (extension.beta.getOrElse(false)) DependencyInfo.fetchJdaBetaVersion() else DependencyInfo.fetchJdaVersion()}")!!)
}
