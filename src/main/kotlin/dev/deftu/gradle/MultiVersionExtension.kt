package dev.deftu.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Property

abstract class MultiVersionExtension(
    val project: Project
) {

    abstract val moveBuildsToRootProject: Property<Boolean>

    init {
        moveBuildsToRootProject.convention(false)
    }

}
