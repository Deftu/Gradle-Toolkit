package dev.deftu.gradle.utils

import dev.deftu.gradle.exceptions.LoaderSpecificException
import org.gradle.api.Project

class MCDependencies(
    val mcData: MCData
) {

    inner class Fabric {

        val fabricLoaderVersion: String
            get() {
                if (!mcData.isFabric) throw LoaderSpecificException(ModLoader.FABRIC)

                return MinecraftInfo.Fabric.LOADER_VERSION
            }

        val yarnVersion: String
            get() {
                if (!mcData.isFabric) throw LoaderSpecificException(ModLoader.FABRIC)

                return MinecraftInfo.Fabric.getYarnVersion(mcData.version)
            }

        val fabricApiVersion: String
            get() {
                if (!mcData.isFabric) throw LoaderSpecificException(ModLoader.FABRIC)

                return MinecraftInfo.Fabric.getFabricApiVersion(mcData.version)
            }

        val fabricLanguageKotlinVersion: String
            get() {
                if (!mcData.isFabric) throw LoaderSpecificException(ModLoader.FABRIC)

                return MinecraftInfo.Fabric.KOTLIN_DEP_VERSION
            }

        val modMenuVersion: String
            get() {
                if (!mcData.isFabric) throw LoaderSpecificException(ModLoader.FABRIC)

                val (_, version) = MinecraftInfo.Fabric.getModMenuDependency(mcData.version)
                return version
            }

        val modMenuDependency: String
            get() {
                if (!mcData.isFabric) throw LoaderSpecificException(ModLoader.FABRIC)

                val (group, version) = MinecraftInfo.Fabric.getModMenuDependency(mcData.version)
                return "$group$version"
            }

    }

    inner class Forge {

        val forgeVersion: String
            get() {
                if (!mcData.isForge) throw LoaderSpecificException(ModLoader.FORGE)

                return MinecraftInfo.Forge.getForgeVersion(mcData.version)
            }

        val mcpDependency: String
            get() {
                if (!mcData.isForge) throw LoaderSpecificException(ModLoader.FORGE)

                return MinecraftInfo.Forge.getMcpDependency(mcData.version)
            }

    }

    inner class NeoForged {

        val neoForgedVersion: String
            get() {
                if (!mcData.isNeoForged) throw LoaderSpecificException(ModLoader.NEOFORGE)

                return MinecraftInfo.NeoForged.getNeoForgedVersion(mcData.version)
            }

    }

    val fabric = Fabric()
    val forge = Forge()
    val neoForged = NeoForged()

}

data class MCData(
    val isPresent: Boolean,
    val version: MinecraftVersion,
    val loader: ModLoader
) {

    val isFabric: Boolean
        get() = loader == ModLoader.FABRIC

    val isForge: Boolean
        get() = loader == ModLoader.FORGE

    val isNeoForged: Boolean
        get() = loader == ModLoader.NEOFORGE

    val isForgeLike: Boolean
        get() = isForge || isNeoForged

    val isModLauncher: Boolean
        get() = loader == ModLoader.FORGE && version >= MinecraftVersion.VERSION_1_14

    val isLegacyForge: Boolean
        get() = loader == ModLoader.FORGE && version < MinecraftVersion.VERSION_1_14

    val dependencies = MCDependencies(this)

    override fun toString(): String {
        return "$version-$loader"
    }

    companion object {

        @JvmStatic
        val versionRegex = "(?<major>\\d+).(?<minor>\\d+).?(?<patch>\\d+)?".toRegex()

        /**
         * Gets the project's Minecraft version, either by the property or by inferring it from the project's name.
         */
        private val Project.minecraftVersion: String
            get() = propertyOr("minecraft.version", name, prefix = false)

        private val Project.modLoader: ModLoader
            get() = ModLoader.from(propertyOr(
                "loom.platform",
                name,
                prefix = false
            ))

        @JvmStatic
        fun from(project: Project): MCData {
            val extension = project.extensions.findByName("mcData") as MCData?
            if (extension != null) return extension

            val isValidProject = project.hasProperty("minecraft.version") || project.isMultiversionProject()
            if (!isValidProject) return MCData(false, MinecraftVersion.UNKNOWN, ModLoader.OTHER)

            val (major, minor, patch) = match(project.minecraftVersion)
            val data = MCData(true, MinecraftVersion.from(major, minor, patch), project.modLoader)
            project.extensions.add("mcData", data)
            return data
        }

        private fun match(version: String): Triple<Int, Int, Int> {
            val match = versionRegex.find(version) ?: throw IllegalArgumentException("Invalid version format: $version")
            val groups = match.groups

            val major = groups["major"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format: $version")
            val minor = groups["minor"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format: $version")
            val patch = groups["patch"]?.value?.toInt() ?: 0
            return Triple(major, minor, patch)
        }

    }
}