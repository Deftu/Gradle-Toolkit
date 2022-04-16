package xyz.unifycraft.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Project

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
        @JvmStatic fun from(project: Project): MCData {
            if (project.hasProperty("minecraft.version") && project.hasProperty("minecraft.loader")) {
                val version = project.property("minecraft.version") as String
                val loader = project.property("minecraft.loader") as String
                val split = version.split(".")
                return MCData(split[0].toInt(), split[1].toInt(), split[2].toInt(), ModLoader.from(loader))
            }

            var name = project.name
            if (name.lastIndexOf("-") != -1)
                name = name.substring(name.lastIndexOf("-") + 1)
            val parts = name.split(".")
            val major = parts[0].toInt()
            val minor = parts[1].toInt()
            val patch = parts[2].toInt()
            val loader = ModLoader.from(project.name)
            return MCData(major, minor, patch, loader)
        }

        @JvmStatic fun from(version: String): MCData {
            val parts = version.split(".")
            val major = parts[0].toInt()
            val minor = parts[1].toInt()
            val patch = parts[2].toInt()
            val loader = ModLoader.from(version)
            return MCData(major, minor, patch, loader)
        }

        @JvmStatic fun fromExisting(project: Project) =
            project.extensions.findByType(MCData::class.java) ?: from(project)
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
        @JvmStatic val forge = ModLoader("forge")
        @JvmStatic val fabric = ModLoader("fabric")
        @JvmStatic val other = ModLoader("other")
        @JvmStatic fun from(version: String): ModLoader {
            return when {
                version.contains("forge") -> forge
                version.contains("fabric") -> fabric
                else -> other.withName(version.substringAfterLast("-"))
            }
        }
    }
}