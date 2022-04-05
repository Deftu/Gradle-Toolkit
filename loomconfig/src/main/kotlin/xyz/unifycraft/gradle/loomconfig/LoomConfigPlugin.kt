package xyz.unifycraft.gradle.loomconfig

import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.LoomGradlePlugin
import org.gradle.api.Project
import xyz.unifycraft.gradle.base.BasePlugin

class LoomConfigPlugin : BasePlugin() {
    override fun onApply(project: Project) {
        val extension = project.extensions.create("loomConfig", LoomConfigExtension::class.java)
        val mcVersionStr = extension.version.getOrElse("")
        if (mcVersionStr.isEmpty()) return
        setup(project, extension, mcVersionStr)
    }

    fun setup(project: Project, extension: LoomConfigExtension, mcVersionStr: String) {
        val mcVersion = fetchVersionFormatted(mcVersionStr)
        val side = extension.side.getOrElse(Side.CLIENT)
        val isForge = extension.forge.getOrElse(false)

        val isArchLoomPresent = try {
            Class.forName("net.fabricmc.loom.api.ForgeExtensionAPI")
            true
        } catch (e: Exception) {
            false
        }

        project.plugins.whenPluginAdded {
            if (it is LoomGradlePlugin) {
                if (isArchLoomPresent) {
                    if (!project.extensions.extraProperties.has("loom.platform"))
                        project.extensions.extraProperties["loom.platform"] = if (isForge) "forge" else "fabric"
                    val loomExtension = project.extensions.getByType(LoomGradleExtension::class.java)
                    loomExtension.runConfigs.getByName(side.toString().lowercase()).isIdeConfigGenerated = true
                }

                assignDependency(project, "net.minecraft:minecraft:$mcVersionStr", "minecraft")
                setupMappingsDependency(project, isForge, mcVersion)
                if (!isForge)
                    setupFabricLoaderDependency(project, mcVersion)
                if (isArchLoomPresent && isForge)
                    setupForgeDependency(project, mcVersion)
            }
        }
    }

    private fun setupMappingsDependency(project: Project, isForge: Boolean, mcVersion: Int) {
        val dependency =
            if (isForge) buildString {
                val mappings = fetchMcpMappings(mcVersion)
                if (!mappings.isNullOrEmpty()) {
                    append("de.oceanlabs.mcp:mcp_$mappings")
                }
            } else "net.fabricmc:yarn:${fetchYarnMappings(mcVersion)}"

        if (dependency.isNotEmpty()) {
            assignDependency(project, dependency, "mappings")
        } else {
            val loomExtension = project.extensions.getByType(LoomGradleExtension::class.java)
            project.dependencies.add("mappings", loomExtension.officialMojangMappings())
            //loomExtension.forge.pack200Provider.set(Pack200A)
        }
    }

    private fun setupFabricLoaderDependency(project: Project, mcVersion: Int) {
        val dependency = fetchFabricLoaderVersion(mcVersion) ?: throw MinecraftVersionException("Could not find a Fabric loader version for Minecraft version $mcVersion.")
        assignDependency(project, dependency, "modImplementation")
    }

    private fun setupForgeDependency(project: Project, mcVersion: Int) {
        val dependency = fetchForgeVersion(mcVersion) ?: throw MinecraftVersionException("Could not find a Forge version for Minecraft version $mcVersion.")
        assignDependency(project, dependency, "forge")
    }

    private fun assignDependency(
        project: Project,
        dependency: String,
        configuration: String
    ) = project.dependencies.add(configuration, dependency)

    companion object {
        @JvmStatic val infoMap: Map<String, Map<Int, String>> = mapOf(
            "fabric_loader_version" to mapOf(
                0 to "0.13.3"
            ), "forge_version" to mapOf(
                11802 to "1.18.1-39.0.79",
                11701 to "1.17.1-37.0.112",
                11605 to "1.16.2-33.0.61",
                11502 to "1.15.2-31.1.18",
                11404 to "1.14.4-28.1.113",
                11202 to "1.12.2-14.23.0.2486",
                10809 to "1.8.9-11.15.1.2318-1.8.9",
                10800 to "1.8-11.14.4.1563",
                10710 to "1.7.10-10.13.4.1558-1.7.10"
            ), "yarn_mappings" to mapOf(
                11802 to "1.18.2+build.2:v2",
                11701 to "1.17.1+build.39:v2",
                11605 to "1.16.4+build.6:v2",
                11502 to "1.15.2+build.14",
                11404 to "1.14.4+build.16"
            ), "mcp_mappings" to mapOf(
                11605 to "snapshot:20201028-1.16.3",
                11502 to "snapshot:20200220-1.15.1@zip",
                11404 to "snapshot:20190719-1.14.3",
                11202 to "snapshot:20170615-1.12",
                10809 to "stable:22-1.8.9",
                10800 to "snapshot:20141130-1.8",
                10710 to "stable:12-1.7.10"
            )
        )

        @JvmStatic fun fetchFabricLoaderVersion(mcVersion: Int) =
            infoMap["fabric_loader_version"]?.get(mcVersion)
        @JvmStatic fun fetchForgeVersion(mcVersion: Int) =
            infoMap["forge_version"]?.get(mcVersion)
        @JvmStatic fun fetchYarnMappings(mcVersion: Int) =
            infoMap["yarn_mappings"]?.get(mcVersion)
        @JvmStatic fun fetchMcpMappings(mcVersion: Int) =
            infoMap["mcp_mappings"]?.get(mcVersion)

        @JvmStatic fun fetchVersionFormatted(version: String): Int {
            val parts = version.split(".")
            val major = parts[0].toInt()
            val minor = parts[1].toInt()
            val patch = parts[2].toInt()
            return major * 10000 + minor * 100 + patch
        }
    }
}