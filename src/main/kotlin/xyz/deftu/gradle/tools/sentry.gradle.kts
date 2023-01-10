package xyz.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.implementation
import xyz.deftu.gradle.DependencyInfo
import xyz.deftu.gradle.utils.shadeOptional

dependencies {
    shadeOptional(implementation("io.sentry:sentry:${DependencyInfo.fetchSentryVersion()}")!!)
}
