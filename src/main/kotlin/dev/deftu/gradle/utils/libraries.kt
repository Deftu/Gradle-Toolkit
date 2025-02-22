@file:JvmName("LibraryHelper")

package dev.deftu.gradle.utils

import gradle.kotlin.dsl.accessors._7f302803de3c8e8ef0ce80f8d318d1c9.implementation
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
