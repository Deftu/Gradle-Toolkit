package dev.deftu.gradle.utils

import dev.deftu.gradle.ToolkitConstants
import dev.deftu.gradle.exceptions.LoaderSpecificException
import dev.deftu.gradle.utils.mcinfo.MinecraftInfo
import dev.deftu.gradle.utils.version.MinecraftVersion
import dev.deftu.gradle.utils.version.MinecraftVersions
import org.gradle.api.Project

class MCDependencies(
    val mcData: MCData
) {

    inner class Fabric {

        val fabricLoaderVersion: String
            get() {
                if (!mcData.isFabric) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FABRIC)
                }

                val fabricLoaderVersionOverride = mcData.project.propertyOr("fabric.loader.version", "")
                if (fabricLoaderVersionOverride.isNotEmpty()) {
                    return fabricLoaderVersionOverride
                }

                return MinecraftInfo.get(mcData.project).fabricLoaderVersion
            }

        val yarnVersion: String
            get() {
                if (!mcData.isFabric) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FABRIC)
                }

                val yarnVersionOverride = mcData.project.propertyOr("fabric.yarn.version", "")
                if (yarnVersionOverride.isNotEmpty()) {
                    return yarnVersionOverride
                }

                return MinecraftInfo.get(mcData.project).getFabricYarnVersion(mcData.version)
            }

        val fabricApiVersion: String
            get() {
                if (!mcData.isFabric) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FABRIC)
                }

                val fabricApiVersionOverride = mcData.project.propertyOr("fabric.api.version", "")
                if (fabricApiVersionOverride.isNotEmpty()) {
                    return fabricApiVersionOverride
                }

                return MinecraftInfo.get(mcData.project).getFabricApiVersion(mcData.version)
            }

        val fabricLanguageKotlinVersion: String
            get() {
                if (!mcData.isFabric) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FABRIC)
                }

                val kotlinVersionOverride = mcData.project.propertyOr("fabric.language.kotlin.version", "")
                if (kotlinVersionOverride.isNotEmpty()) {
                    return kotlinVersionOverride
                }

                return MinecraftInfo.get(mcData.project).fabricLanguageKotlinVersion
            }

        val modMenuVersion: String
            get() {
                if (!mcData.isFabric) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FABRIC)
                }

                val modMenuVersionOverride = mcData.project.propertyOr("fabric.modmenu.version", "")
                if (modMenuVersionOverride.isNotEmpty()) {
                    return modMenuVersionOverride
                }

                val (_, version) = MinecraftInfo.get(mcData.project).getFabricModMenuDefinition(mcData.version)
                return version
            }

        val modMenuDependency: String
            get() {
                if (!mcData.isFabric) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FABRIC)
                }

                val modMenuDependencyOverride = mcData.project.propertyOr("fabric.modmenu.dependency", "")
                if (modMenuDependencyOverride.isNotEmpty()) {
                    return modMenuDependencyOverride
                }

                val (group, version) = MinecraftInfo.get(mcData.project).getFabricModMenuDefinition(mcData.version)
                return "$group$version"
            }

    }

    inner class LegacyFabric {

        val legacyYarnVersion: String
            get() {
                if (!mcData.isLegacyFabric) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FABRIC)
                }

                val legacyYarnVersionOverride = mcData.project.propertyOr("fabric.yarn.version", "")
                if (legacyYarnVersionOverride.isNotEmpty()) {
                    return legacyYarnVersionOverride
                }

                return MinecraftInfo.get(mcData.project).getLegacyFabricYarnVersion(mcData.version)
            }

        val legacyFabricApiVersion: String
            get() {
                if (!mcData.isLegacyFabric) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FABRIC)
                }

                val legacyFabricApiVersionOverride = mcData.project.propertyOr("fabric.api.version", "")
                if (legacyFabricApiVersionOverride.isNotEmpty()) {
                    return legacyFabricApiVersionOverride
                }

                return MinecraftInfo.get(mcData.project).getLegacyFabricApiVersion(mcData.version)
            }

    }

    inner class ForgeLike {

        val kotlinForForgeVersion: String
            get() {
                if (!mcData.isForgeLike) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FORGE, ModLoader.NEOFORGE)
                }

                val kotlinForForgeVersionOverride = mcData.project.propertyOr("forge.kotlin.version", "")
                if (kotlinForForgeVersionOverride.isNotEmpty()) {
                    return kotlinForForgeVersionOverride
                }

                return MinecraftInfo.get(mcData.project).getKotlinForForgeVersion(mcData.version)
            }

    }

    inner class Forge {

        val forgeVersion: String
            get() {
                if (!mcData.isForgeLike) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FORGE)
                }

                val forgeVersionOverride = mcData.project.propertyOr("forge.version", "")
                if (forgeVersionOverride.isNotEmpty()) {
                    return forgeVersionOverride
                }

                return MinecraftInfo.get(mcData.project).getForgeVersion(mcData.version)
            }

        val mcpDependency: String
            get() {
                if (!mcData.isForge) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.FORGE)
                }

                val mcpDependencyOverride = mcData.project.propertyOr("forge.mcp.dependency", "")
                if (mcpDependencyOverride.isNotEmpty()) {
                    return mcpDependencyOverride
                }

                return MinecraftInfo.get(mcData.project).getMcpDefinition(mcData.version)
            }

    }

    inner class NeoForged {

        val neoForgeVersion: String
            get() {
                if (!mcData.isNeoForge) {
                    throw LoaderSpecificException(mcData.loader, ModLoader.NEOFORGE)
                }

                val neoForgeVersionOverride = mcData.project.propertyOr("neoforge.version", "")
                if (neoForgeVersionOverride.isNotEmpty()) {
                    return neoForgeVersionOverride
                }

                return MinecraftInfo.get(mcData.project).getNeoForgeVersion(mcData.version)
            }

    }

    val fabric = Fabric()
    val legacyFabric = LegacyFabric()
    val forgeLike = ForgeLike()
    val forge = Forge()
    val neoForged = NeoForged()

}

data class MCData(
    val project: Project,
    val isPresent: Boolean,
    val version: MinecraftVersion<*>,
    val loader: ModLoader
) {

    val isFabric: Boolean
        get() = loader == ModLoader.FABRIC

    val isLegacyFabric: Boolean
        get() = loader == ModLoader.FABRIC && version < MinecraftVersions.VERSION_1_13_2

    val isForge: Boolean
        get() = loader == ModLoader.FORGE

    val isNeoForge: Boolean
        get() = loader == ModLoader.NEOFORGE

    val isForgeLike: Boolean
        get() = isForge || isNeoForge

    val isModLauncher: Boolean
        get() = loader == ModLoader.FORGE && version >= MinecraftVersions.VERSION_1_14

    val isLegacyForge: Boolean
        get() = loader == ModLoader.FORGE && version < MinecraftVersions.VERSION_1_14

    val dependencies = MCDependencies(this)

    override fun toString(): String {
        return "$version-$loader"
    }

    companion object {

        /**
         * Gets the project's Minecraft version, either by the property or by inferring it from the project's name.
         */
        private val Project.minecraftVersion: String
            get() = propertyOr("minecraft.version", name, prefix = "")

        private val Project.modLoader: ModLoader
            get() = ModLoader.from(propertyOr(
                "loom.platform",
                name,
                prefix = ""
            ))

        @JvmStatic
        fun from(project: Project): MCData {
            val extension = project.extensions.findByName("mcData") as MCData?
            if (extension != null) {
                return extension
            }

            val isValidProject = project.hasProperty("minecraft.version") || project.isMultiversionProject()
            if (!isValidProject) {
                if (ToolkitConstants.debug) {
                    project.logger.warn("Project ${project.name} is not a valid Minecraft-relating project.")
                }

                return MCData(project, false, MinecraftVersions.UNKNOWN, ModLoader.OTHER)
            }

            val version = MinecraftVersions.get(project, project.minecraftVersion)
            val data = MCData(project, true, version, project.modLoader)
            project.extensions.add("mcData", data)
            return data
        }

    }
}