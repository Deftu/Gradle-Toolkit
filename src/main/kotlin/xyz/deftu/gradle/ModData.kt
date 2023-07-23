package xyz.deftu.gradle

import org.gradle.api.Project
import xyz.deftu.gradle.utils.propertyOr
import java.util.*

data class ModData(
    val present: Boolean,
    val name: String,
    val id: String,
    val version: String,
    val group: String,
    val description: String
) {
    companion object {
        @JvmStatic
        fun from(project: Project): ModData {
            val extension = project.extensions.findByName("modData") as ModData?
            if (extension != null) return extension

            if (!project.hasProperty("mod.id")) return ModData(false, "", "", "", "", "")

            val name = project.propertyOr("mod.name", project.name, false)
            val id = project.propertyOr("mod.id", name.toLowerCase(Locale.US).replace(" ", "_"), false)
            val version = project.propertyOr("mod.version", project.version.toString(), false)
            val group = project.propertyOr("mod.group", project.group.toString(), false)
            val description = project.propertyOr("mod.description", "", false)
            val data = ModData(true, name, id, version, group, description)
            project.extensions.add("modData", data)
            return data
        }
    }
}
