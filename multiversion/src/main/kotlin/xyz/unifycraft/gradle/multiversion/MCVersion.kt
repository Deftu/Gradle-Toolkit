package xyz.unifycraft.gradle.multiversion

import org.gradle.api.JavaVersion
import org.gradle.api.Project

data class MCVersion(
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
        get() = loader == ModLoader.FABRIC
    val isForge: Boolean
        get() = loader == ModLoader.FORGE

    val javaVersion: JavaVersion
        get() = when {
            version >= 11800 -> JavaVersion.VERSION_17
            version >= 11700 -> JavaVersion.VERSION_16
            else -> JavaVersion.VERSION_1_8
        }

    companion object {
        @JvmStatic fun from(project: Project): MCVersion {
            val parts = project.name.substring(0, project.name.indexOf("-")).split(".")
            project.logger.lifecycle(parts.joinToString(" | "))
            val major = parts[0].toInt()
            val minor = parts[1].toInt()
            val patch = parts[2].toInt()
            val loader = ModLoader.from(project.name)
            return MCVersion(major, minor, patch, loader)
        }

        @JvmStatic fun from(version: String): MCVersion {
            val parts = version.split(".")
            val major = parts[0].toInt()
            val minor = parts[1].toInt()
            val patch = parts[2].toInt()
            val loader = ModLoader.from(version)
            return MCVersion(major, minor, patch, loader)
        }
    }
}

enum class ModLoader {
    FORGE,
    FABRIC;
    companion object {
        fun from(version: String): ModLoader {
            return when {
                version.contains("forge") -> FORGE
                version.contains("fabric") -> FABRIC
                else -> throw IllegalArgumentException("Unknown mod loader for version $version")
            }
        }
    }
}