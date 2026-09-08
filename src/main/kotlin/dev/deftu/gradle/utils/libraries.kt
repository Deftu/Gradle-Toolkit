@file:JvmName("LibraryHelper")

package dev.deftu.gradle.utils

import gradle.kotlin.dsl.accessors._f3108da4a4abff062bb715b70c2940a4.implementation
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope

// Kotlin DSL
fun DependencyHandlerScope.setupJetBrainsExposed(version: String, apply: (String) -> Unit = { dependency -> implementation(dependency) }, vararg modules: String) {
    for (module in modules) {
        apply("org.jetbrains.exposed:exposed-$module:$version")
    }
}

// Gradle Groovy
fun setupJetBrainsExposed(dependencies: DependencyHandler, version: String, apply: (String) -> Unit = { dependency -> dependencies.implementation(dependency) }, vararg modules: String) {
    for (module in modules) {
        apply("org.jetbrains.exposed:exposed-$module:$version")
    }
}
