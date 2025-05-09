package dev.deftu.gradle.utils

import org.gradle.api.Project

class ProjectData(
    val isPresent: Boolean,
    override val name: String,
    override val version: String,
    override val group: String,
    val description: String
) : ProjectInfo {

    companion object {

        @JvmStatic
        val EMPTY = ProjectData(false, "", "", "", "")

        @JvmStatic
        fun from(project: Project): ProjectData {
            val extension = project.extensions.findByName("projectData") as ProjectData?
            if (extension != null) return extension

            if (!project.hasProperty("project.group")) return EMPTY

            val name = project.propertyOr("project.name", project.name, prefix = "")
            val version = project.propertyOr("project.version", project.version.toString(), prefix = "")
            val group = project.propertyOr("project.group", project.group.toString(), prefix = "")
            val description = project.propertyOr("project.description", "", prefix = "")
            val data = ProjectData(true, name, version, group, description)
            project.extensions.add("projectData", data)
            return data
        }

    }

}
