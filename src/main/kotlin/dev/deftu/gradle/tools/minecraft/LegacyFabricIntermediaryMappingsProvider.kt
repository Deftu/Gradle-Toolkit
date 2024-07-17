package dev.deftu.gradle.tools.minecraft

import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.configuration.providers.mappings.IntermediaryMappingsProvider
import org.gradle.api.Project

abstract class LegacyFabricIntermediaryMappingsProvider : IntermediaryMappingsProvider() {

    private lateinit var project: Project
    private lateinit var name: String

    fun configure(project: Project) {
        this.project = project

        val extension = project.extensions.findByType(LegacyFabricExtension::class.java)!!

        intermediaryUrl
            .convention(extension.intermediaryVersion.map { version ->
                val endpoint = when (version) {
                    2 -> "v2/"
                    else -> ""
                }

                "https://maven.legacyfabric.net/net/legacyfabric/${endpoint}intermediary/%1\$s/intermediary-%1\$s-v2.jar"
            }).finalizeValueOnRead()

        refreshDeps.set(project.provider { LoomGradleExtension.get(project).refreshDeps() })
    }

    override fun getName(): String {
        if (!this::name.isInitialized)
            this.name = project.extensions.findByType(LegacyFabricExtension::class.java)!!
                .intermediaryVersion
                .map { version ->
                    "legacy-intermediary-$version-v2"
                }.get()

        return this.name
    }

}
