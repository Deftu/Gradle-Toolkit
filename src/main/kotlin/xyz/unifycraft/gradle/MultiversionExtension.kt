package xyz.unifycraft.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Property

abstract class MultiversionExtension(
    project: Project
) {
    abstract val mcVersion: Property<MCVersion>

    init {
        mcVersion.convention(MCVersion.from(project))
    }
}