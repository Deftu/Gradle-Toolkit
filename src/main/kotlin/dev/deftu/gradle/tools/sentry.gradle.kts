package dev.deftu.gradle.tools

import dev.deftu.gradle.DependencyInfo
import dev.deftu.gradle.utils.shadeOptionally
import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.implementation

dependencies {
    shadeOptionally(implementation("io.sentry:sentry:${DependencyInfo.fetchSentryVersion()}")!!)
}
