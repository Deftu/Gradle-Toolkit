package xyz.deftu.gradle.tools.minecraft

import gradle.kotlin.dsl.accessors._e453d737b2e0a36dae3d829967a3e2e0.loom
import gradle.kotlin.dsl.accessors._e453d737b2e0a36dae3d829967a3e2e0.mappings
import gradle.kotlin.dsl.accessors._e453d737b2e0a36dae3d829967a3e2e0.minecraft
import gradle.kotlin.dsl.accessors._e453d737b2e0a36dae3d829967a3e2e0.modImplementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import xyz.deftu.gradle.GameInfo.FABRIC_LOADER_VERSION
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
extensions.create("toolkitLoomHelper", LoomHelperExtension::class)
extra.set("loom.platform", if (mcData.isFabric) "fabric" else "forge")

loom {
    runConfigs {
        all {
            isIdeConfigGenerated = true
        }
    }
}

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
            modImplementation(propertyOr("loom.fabricloader", "net.fabricmc:fabric-loader:${FABRIC_LOADER_VERSION}"))
        } else {
            "forge"(propertyOr("loom.forge", "net.minecraftforge:forge:${fetchForgeVersion(mcData.version)}"))
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
