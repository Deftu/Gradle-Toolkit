package dev.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._175fcbe9e04105e3219d6e2795d7218e.implementation
import dev.deftu.gradle.DependencyInfo
import dev.deftu.gradle.utils.shadeOptionally

dependencies {
    shadeOptionally(implementation("io.sentry:sentry:${DependencyInfo.fetchSentryVersion()}")!!)
}
