package xyz.unifycraft.gradle.multiversion

import org.gradle.api.Project
import org.gradle.api.provider.Property

abstract class MultiversionExtension(
    project: Project
) {
    abstract val mcVersion: Property<MCVersion>

    abstract val assignMinecraftDependency: Property<Boolean>
    abstract val assignMappingsDependency: Property<Boolean>
    abstract val assignFabricLoaderDependency: Property<Boolean>
    abstract val assignForgeDependency: Property<Boolean>
    init {
        mcVersion.convention(MCVersion.from(project))

        assignMinecraftDependency.convention(true)
        assignMappingsDependency.convention(true)
        assignFabricLoaderDependency.convention(true)
        assignForgeDependency.convention(true)
    }
}