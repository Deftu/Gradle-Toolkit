package xyz.unifycraft.gradle

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
        get() = loader == ModLoader.fabric
    val isForge: Boolean
        get() = loader == ModLoader.forge

    val javaVersion: JavaVersion
        get() = when {
            version >= 11800 -> JavaVersion.VERSION_17
            version >= 11700 -> JavaVersion.VERSION_16
            else -> JavaVersion.VERSION_1_8
        }

    companion object {
        @JvmStatic fun from(project: Project): MCVersion {
            val parts = project.name.substring(0, project.name.indexOf("-")).split(".")
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

class ModLoader(
    name: String
) {
    var name: String = name
        private set
    fun withName(name: String) = apply {
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