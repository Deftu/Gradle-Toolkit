package dev.deftu.gradle.tools

import org.gradle.api.Project
import org.gradle.api.provider.Property

abstract class MavenPublishingExtension(
    val project: Project
) {
    abstract val artifactName: Property<String>
    abstract val setupPublication: Property<Boolean>
    abstract val setupRepositories: Property<Boolean>

    init {
        setupPublication.convention(true)
        setupRepositories.convention(true)
    }
}
