package dev.deftu.gradle

import groovy.lang.MissingPropertyException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import dev.deftu.gradle.utils.isMultiversionProject
import dev.deftu.gradle.utils.propertyOr

data class MCData(
    val present: Boolean,
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
    val minorVersionStr: String
        get() = listOf(major, minor).joinToString(".")

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
            version >= 1_20_06 -> JavaVersion.VERSION_21
            version >= 1_18_00 -> JavaVersion.VERSION_17
            version >= 1_17_00 -> JavaVersion.VERSION_16
            else -> JavaVersion.VERSION_1_8
        }

    val fabricApiVersion: String
        get() = GameInfo.fetchFabricApiVersion(version) ?: GameInfo.fetchLatestFabricApiVersion()!!
    val fabricLanguageKotlinVersion: String
        get() = GameInfo.FABRIC_LANGUAGE_KOTLIN_VERSION
    val modMenuDependency: String
        get() {
            val (group, version) = GameInfo.fetchModMenuVersion(version) ?: GameInfo.fetchLatestModMenuVersion()!!
            return "$group$version"
        }

    val forgeVersion: String
        get() = GameInfo.fetchForgeVersion(version) ?: GameInfo.fetchLatestForgeVersion()!!
    val yarnMappings: String
        get() = GameInfo.fetchYarnMappings(version) ?: GameInfo.fetchLatestYarnMappings()!!
    val mcpMappings: String
        get() = GameInfo.fetchMcpMappings(version) ?: GameInfo.fetchLatestMcpMappings()!!

    companion object {
        @JvmStatic
        val versionRegex = "(?<major>\\d+).(?<minor>\\d+).?(?<patch>\\d+)?".toRegex()

        @JvmStatic
        fun from(project: Project): MCData {
            val extension = project.extensions.findByName("mcData") as MCData?
            if (extension != null) return extension

            val valid = project.hasProperty("minecraft.version") || project.isMultiversionProject()
            if (!valid) return MCData(false, 0, 0, 0, ModLoader.other)

            val version = project.propertyOr("minecraft.version", project.name, false) // Either get it from the property or infer it from the project's name for multi-version projects.
            val versionMatch = (versionRegex.find(version) ?: throw MissingPropertyException("Couldn't fetch Minecraft version. Either set the minecraft.version property in your Gradle properties file or define the Minecraft version in the project's name.")).groups
            val loader = ModLoader.from(project.propertyOr("minecraft.loader", project.propertyOr("loom.platform", project.name, false)), !project.hasProperty("minecraft.version"))
            val data = MCData(true, versionMatch["major"]!!.value.toInt(), versionMatch["minor"]!!.value.toInt(), versionMatch["patch"]?.value?.toInt() ?: 0, loader)
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
        val all = listOf(forge, fabric, other)

        @JvmStatic
        fun from(input: String, multiversion: Boolean): ModLoader {
            return when {
                input.contains("forge") -> forge
                input.contains("fabric") -> fabric
                else -> if (!multiversion) other.withName(input) else other.withName(input.substringAfterLast("-")) // Multi-version projects (should) have a "-<loader>" suffix.
            }
        }
    }
}