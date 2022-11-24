package xyz.deftu.gradle

import org.gradle.api.Project
import xyz.deftu.gradle.utils.propertyOr

data class ModData(
    val present: Boolean,
    val name: String,
    val id: String,
    val version: String,
    val group: String
) {
    companion object {
        @JvmStatic
        fun from(project: Project): ModData {
            val extension = project.extensions.findByName("modData") as ModData?
            if (extension != null) return extension

            if (!project.hasProperty("mod.id")) return ModData(false, "", "", "", "")

            val name = project.propertyOr("mod.name", project.name, false)
            val id = project.propertyOr("mod.id", name.toLowerCase().replace(" ", "_"), false)
            val version = project.propertyOr("mod.version", project.version.toString(), false)
            val group = project.propertyOr("mod.group", project.group.toString(), false)
            val data = ModData(true, name, id, version, group)
            project.extensions.add("modData", data)
            return data
        }
    }
}
