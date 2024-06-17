package dev.deftu.gradle.tools.publishing

import org.gradle.api.Project
import org.gradle.api.provider.Property

abstract class MavenPublishingExtension(
    val project: Project
) {
    abstract val artifactName: Property<String>
    abstract val forceLowercase: Property<Boolean>
    abstract val setupPublication: Property<Boolean>
    abstract val setupRepositories: Property<Boolean>

    init {
        forceLowercase.convention(true)
        setupPublication.convention(true)
        setupRepositories.convention(true)
    }
}
