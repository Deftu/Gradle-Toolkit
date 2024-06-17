package dev.deftu.gradle.utils

import org.gradle.api.Project

class ProjectData(
    val isPresent: Boolean,
    val name: String,
    val version: String,
    val group: String,
    val description: String
) {

    companion object {

        @JvmStatic
        val EMPTY = ProjectData(false, "", "", "", "")

        @JvmStatic
        fun from(project: Project): ProjectData {
            val extension = project.extensions.findByName("projectData") as ProjectData?
            if (extension != null) return extension

            if (!project.hasProperty("project.group")) return EMPTY

            val name = project.propertyOr("project.name", project.name, false)
            val version = project.propertyOr("project.version", project.version.toString(), false)
            val group = project.propertyOr("project.group", project.group.toString(), false)
            val description = project.propertyOr("project.description", "", false)
            val data = ProjectData(true, name, version, group, description)
            project.extensions.add("projectData", data)
            return data
        }

    }

}
