package dev.deftu.gradle.tools.minecraft

import gradle.kotlin.dsl.accessors._e453d737b2e0a36dae3d829967a3e2e0.loom
import gradle.kotlin.dsl.accessors._e453d737b2e0a36dae3d829967a3e2e0.mappings
import gradle.kotlin.dsl.accessors._e453d737b2e0a36dae3d829967a3e2e0.minecraft
import gradle.kotlin.dsl.accessors._e453d737b2e0a36dae3d829967a3e2e0.modImplementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import dev.deftu.gradle.GameInfo.FABRIC_LOADER_VERSION
import dev.deftu.gradle.GameInfo.fetchForgeVersion
import dev.deftu.gradle.GameInfo.fetchMcpMappings
import dev.deftu.gradle.GameInfo.fetchYarnMappings
import dev.deftu.gradle.MCData
import dev.deftu.gradle.utils.propertyBoolOr
import dev.deftu.gradle.utils.propertyOr

plugins {
    id("gg.essential.loom")
}

val mcData = MCData.from(project)
val extension = extensions.create("toolkitLoomHelper", LoomHelperExtension::class)
extra.set("loom.platform", if (mcData.isFabric) "fabric" else "forge")

loom {
    runConfigs {
        all {
            isIdeConfigGenerated = true
        }
    }

    if (mcData.isLegacyForge) {
        forge {
            pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
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

afterEvaluate {
    if (extension.appleSiliconFix.get()) {
        if (
            System.getProperty("os.arch") == "aarch64" &&
            System.getProperty("os.name") == "Mac OS X"
        ) {
            val lwjglVersion = if (mcData.version >= 1_19_00) "3.3.1" else "3.3.0"
            val lwjglNatives = "natives-macos-arm64"
            logger.error("Apple Silicon for Minecraft ${mcData.versionStr} ($lwjglVersion, $lwjglNatives)")

            configurations.all {
                resolutionStrategy {
                    force("org.lwjgl:lwjgl:$lwjglVersion")
                    force("org.lwjgl:lwjgl-openal:$lwjglVersion")
                    force("org.lwjgl:lwjgl-opengl:$lwjglVersion")
                    force("org.lwjgl:lwjgl-jemalloc:$lwjglVersion")
                    force("org.lwjgl:lwjgl-glfw:$lwjglVersion")
                    force("org.lwjgl:lwjgl-stb:$lwjglVersion")
                    force("org.lwjgl:lwjgl-tinyfd:$lwjglVersion")
                }
            }
        }
    }
}
