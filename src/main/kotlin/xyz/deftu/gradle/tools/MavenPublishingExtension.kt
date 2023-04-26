package xyz.deftu.gradle.tools

import org.gradle.api.Project
import org.gradle.api.provider.Property

abstract class MavenPublishingExtension(
    val project: Project
) {
    abstract val artifactName: Property<String>
}
