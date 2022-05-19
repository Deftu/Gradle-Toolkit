package xyz.unifycraft.gradle

import org.gradle.api.Project
import xyz.unifycraft.gradle.utils.propertyOr

data class ModData(
    val name: String,
    val id: String,
    val version: String,
    val group: String
) {
    companion object {
        @JvmStatic
        fun from(project: Project): ModData {
            val extension = project.extensions.findByName("modData") as ModData?
            if (extension != null)
                return extension

            val name = project.propertyOr("mod.name", project.name)!!
            val id = project.propertyOr("mod.id", name.toLowerCase().replace(" ", "_"))!!
            val version = project.propertyOr("mod.version", project.version.toString())!!
            val group = project.propertyOr("mod.group", "")!!
            val data = ModData(name, id, version, group)
            project.extensions.add("modData", data)
            return data
        }
    }
}
