package xyz.deftu.gradle.tools

import dev.architectury.pack200.java.Pack200Adapter
import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.loom
import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.mappings
import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.minecraft
import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.modImplementation
import org.gradle.kotlin.dsl.dependencies
import xyz.deftu.gradle.GameInfo.fetchFabricLoaderVersion
import xyz.deftu.gradle.GameInfo.fetchForgeVersion
import xyz.deftu.gradle.GameInfo.fetchMcpMappings
import xyz.deftu.gradle.GameInfo.fetchYarnMappings
import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.utils.propertyBoolOr
import xyz.deftu.gradle.utils.propertyOr

plugins {
    id("gg.essential.loom")
}

val mcData = MCData.from(project)
extensions.create("loomHelper", LoomHelperExtension::class.java)
extra.set("loom.platform", if (mcData.isFabric) "fabric" else "forge")
dependencies {
    if (propertyBoolOr("loom.minecraft.use", true)) minecraft(propertyOr("loom.minecraft", "com.mojang:minecraft:${mcData.versionStr}"))

    if (propertyBoolOr("loom.mappings.use", true)) {
        propertyOr(
            "loom.mappings", when {
                mcData.isForge && mcData.version <= 11502  -> "de.oceanlabs.mcp:mcp_${fetchMcpMappings(mcData.version)}"
                mcData.isFabric -> "net.fabricmc:yarn:${fetchYarnMappings(mcData.version)}"
                else -> "official"
            }
        ).apply {
            if (this == "official") {
                mappings(loom.officialMojangMappings())
            } else {
                mappings(this)
            }
        }
    }

    if (propertyBoolOr("loom.loader.use", true)) {
        if (mcData.isFabric) {
            modImplementation(propertyOr("loom.fabricloader", "net.fabricmc:fabric-loader:${fetchFabricLoaderVersion(0)}"))
        } else {
            "forge"(propertyOr("loom.forge", "net.minecraftforge:forge:${fetchForgeVersion(mcData.version)}"))
            loom.forge.pack200Provider.set(Pack200Adapter())
        }
    }
}

// https://github.com/architectury/architectury-loom/pull/10
if (mcData.isModLauncher) {
    (repositories.find {
        it.name.contains("Forge", ignoreCase = true)
    } as? MavenArtifactRepository)?.metadataSources {
        mavenPom()
        artifact()
        ignoreGradleMetadataRedirection()
    }
}
