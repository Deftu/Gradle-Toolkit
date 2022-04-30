package xyz.unifycraft.gradle.snippets

import dev.architectury.pack200.java.Pack200Adapter
import dev.architectury.pack200.java.Pack200Plugin
import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.loom
import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.mappings
import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.minecraft
import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.modImplementation
import org.gradle.kotlin.dsl.dependencies
import xyz.unifycraft.gradle.GameInfo.fetchFabricLoaderVersion
import xyz.unifycraft.gradle.GameInfo.fetchForgeVersion
import xyz.unifycraft.gradle.GameInfo.fetchMcpMappings
import xyz.unifycraft.gradle.GameInfo.fetchYarnMappings
import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.MODGRADLE_ID
import xyz.unifycraft.gradle.utils.propertyOr
import xyz.unifycraft.gradle.utils.registerMinecraftData

plugins {
    id(MODGRADLE_ID)
}

val mcData = MCData.fromExisting(project)
registerMinecraftData(mcData)
dependencies {
    minecraft(propertyOr("loom", "minecraft", "com.mojang:minecraft:${mcData.versionStr}"))

    val mappingsDependency = propertyOr(
        "loom", "mappings", when {
            mcData.isForge && mcData.version < 11700  -> "de.oceanlabs.mcp:mcp_${fetchMcpMappings(mcData.version)}"
            mcData.isFabric -> "net.fabricmc:yarn:${fetchYarnMappings(mcData.version)}"
            else -> "official"
        }
    )
    if (mappingsDependency == "official") {
        mappings(loom.officialMojangMappings())
    } else {
        mappings(mappingsDependency)
    }

    if (mcData.isFabric) {
        modImplementation(propertyOr("loom", "fabricloader", "net.fabricmc:fabric-loader:${fetchFabricLoaderVersion(0)}"))
    } else {
        "forge"(propertyOr("loom", "forge", "net.minecraftforge:forge:${fetchForgeVersion(mcData.version)}"))
    }
}

// https://github.com/architectury/architectury-loom/pull/10
if (mcData.isModLauncher) {
    (repositories.find {
        it.name == "Forge"
    } as? MavenArtifactRepository)?.metadataSources {
        mavenPom()
        artifact()
        ignoreGradleMetadataRedirection()
    }
}
