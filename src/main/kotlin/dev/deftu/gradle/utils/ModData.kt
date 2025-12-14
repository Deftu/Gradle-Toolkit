package dev.deftu.gradle.utils

import org.gradle.api.Project
import java.util.*

data class ModData(
    val isPresent: Boolean,
    override val name: String,
    val id: String,
    override val version: String,
    override val group: String,
    val description: String
) : ProjectInfo {
    companion object {
        @JvmStatic val EMPTY = ModData(false, "", "", "", "", "")

        @JvmStatic
        fun from(project: Project): ModData {
            val extension = project.extensions.findByName("modData") as ModData?
            if (extension != null) {
                return extension
            }

            if (!project.hasProperty("mod.id")) {
                return EMPTY
            }

            val name = project.propertyOr("mod.name", project.name, prefix = "")
            val id = project.propertyOr("mod.id", name.lowercase(Locale.US).replace(" ", "_"), prefix = "")
            val version = project.propertyOr("mod.version", project.version.toString(), prefix = "")
            val group = project.propertyOr("mod.group", project.group.toString(), prefix = "")
            val description = project.propertyOr("mod.description", "", prefix = "")
            val data = ModData(true, name, id, version, group, description)
            project.extensions.add("modData", data)
            return data
        }

        @JvmStatic
        fun populateFrom(project: Project, projectData: ProjectData) {
            val extension = project.extensions.findByName("modData") as ModData?
            if (extension != null) {
                return
            }

            if (!project.hasProperty("mod.id")) {
                return
            }

            val id = project.propertyOr("mod.id", projectData.name.lowercase(Locale.US).replace(" ", "_"), prefix = "")
            val data = ModData(true, projectData.name, id, projectData.version, projectData.group, projectData.description)
            project.extensions.add("modData", data)
        }
    }
}
