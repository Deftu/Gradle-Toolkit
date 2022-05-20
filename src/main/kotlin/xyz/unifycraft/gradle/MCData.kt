package xyz.unifycraft.gradle

import groovy.lang.MissingPropertyException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import xyz.unifycraft.gradle.utils.propertyOr

data class MCData(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val loader: ModLoader
) {
    val version: Int
        get() = major * 10000 + minor * 100 + patch
    val versionStr: String
        get() = listOf(major, minor, patch).dropLastWhile {
            it == 0
        }.joinToString(".")

    val isFabric: Boolean
        get() = loader == ModLoader.fabric
    val isForge: Boolean
        get() = loader == ModLoader.forge
    val isModLauncher: Boolean
        get() = loader == ModLoader.forge && version >= 11400
    val isLegacyForge: Boolean
        get() = loader == ModLoader.forge && version < 11400

    val javaVersion: JavaVersion
        get() = when {
            version >= 11800 -> JavaVersion.VERSION_17
            version >= 11700 -> JavaVersion.VERSION_16
            else -> JavaVersion.VERSION_1_8
        }

    companion object {
        @JvmStatic
        fun from(project: Project): MCData {
            val extension = project.extensions.findByName("mcData") as MCData?
            if (extension != null)
                return extension

            project.logger.lifecycle("> Minecraft data not set for ${project.name}. Generating it now.")

            if (project.hasProperty("minecraft.version") && (project.hasProperty("minecraft.loader") || project.hasProperty("loom.platform"))) {
                project.logger.lifecycle("> Minecraft data found in project properties for ${project.name}. Using this.")
                val version = project.propertyOr("minecraft.version") ?: throw MissingPropertyException("minecraft.version")
                val loader = project.propertyOr("minecraft.loader", project.propertyOr("loom.platform", null)) ?: throw MissingPropertyException("minecraft.loader")
                val split = version.split(".")
                val data = MCData(split[0].toInt(), split[1].toInt(), split[2].toInt(), ModLoader.from(loader))
                project.extensions.add("mcData", data)
                return data
            }

            var name = project.name
            if ("-" in name)
                name = name.substring(0, name.indexOf("-"))
            val parts = name.split(".")
            val major = parts[0].toInt()
            val minor = parts[1].toInt()
            val patch = parts[2].toInt()
            val loader = ModLoader.from(project.name)
            val data = MCData(major, minor, patch, loader)
            project.extensions.add("mcData", data)
            return data
        }
    }
}

data class ModLoader(
    private val theName: String
) {
    var name: String = theName
        private set
    fun withName(name: String) = copy().apply {
        this.name = name
    }

    companion object {
        @JvmStatic
        val forge = ModLoader("forge")
        @JvmStatic
        val fabric = ModLoader("fabric")
        @JvmStatic
        val other = ModLoader("other")

        @JvmStatic
        fun from(version: String): ModLoader {
            return when {
                version.contains("forge") -> forge
                version.contains("fabric") -> fabric
                else -> other.withName(version.substringAfterLast("-"))
            }
        }
    }
}