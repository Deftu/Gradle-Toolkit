package dev.deftu.gradle.utils

import org.gradle.api.Project
import java.util.*

data class ModData(
    val isPresent: Boolean,
    val name: String,
    val id: String,
    val version: String,
    val group: String,
    val description: String
) {

    companion object {

        @JvmStatic
        val EMPTY = ModData(false, "", "", "", "", "")

        @JvmStatic
        fun from(project: Project): ModData {
            val extension = project.extensions.findByName("modData") as ModData?
            if (extension != null) return extension

            if (!project.hasProperty("mod.id")) return EMPTY

            val name = project.propertyOr("mod.name", project.name, false)
            val id = project.propertyOr("mod.id", name.lowercase(Locale.US).replace(" ", "_"), false)
            val version = project.propertyOr("mod.version", project.version.toString(), false)
            val group = project.propertyOr("mod.group", project.group.toString(), false)
            val description = project.propertyOr("mod.description", "", false)
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

            project.logger.lifecycle("Populating ModData from ProjectData of ${projectData.name}")
            val id = project.propertyOr("mod.id", projectData.name.lowercase(Locale.US).replace(" ", "_"), false)
            val data = ModData(true, projectData.name, id, projectData.version, projectData.group, projectData.description)
            project.extensions.add("modData", data)
        }

    }

}
