package dev.deftu.gradle.tools.minecraft

import dev.deftu.gradle.utils.IntermediarySource
import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.configuration.providers.mappings.IntermediaryMappingsProvider
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

abstract class ToolkitIntermediaryMappingsProvider : IntermediaryMappingsProvider() {

    abstract val source: Property<IntermediarySource>

    fun configure(project: Project, source: Provider<IntermediarySource>) {
        this.source.convention(source).finalizeValueOnRead()

        intermediaryUrl
            .convention(this.source.map(IntermediarySource::urlTemplate))
            .finalizeValueOnRead()

        refreshDeps.set(project.provider { LoomGradleExtension.get(project).refreshDeps() })
    }

    override fun getName(): String {
        return source.get().providerName
    }

}
