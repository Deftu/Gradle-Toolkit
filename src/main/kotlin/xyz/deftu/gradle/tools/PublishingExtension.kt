package xyz.deftu.gradle.tools

import org.gradle.api.Project
import org.gradle.api.provider.Property
import xyz.deftu.gradle.ModData

abstract class PublishingExtension(
    val project: Project
) {
    abstract val artifactName: Property<String>

    init {
        val modData = ModData.from(project)

        artifactName.convention(modData.name)
    }
}
