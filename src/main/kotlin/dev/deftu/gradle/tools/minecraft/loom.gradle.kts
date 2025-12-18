@file:Suppress("UnstableApiUsage")
package dev.deftu.gradle.tools.minecraft

import dev.architectury.pack200.java.Pack200Adapter
import dev.deftu.gradle.ToolkitConstants
import org.gradle.kotlin.dsl.dependencies
import dev.deftu.gradle.utils.*
import dev.deftu.gradle.utils.mcinfo.MinecraftInfo
import dev.deftu.gradle.utils.version.MinecraftVersions

// Set XML parsers so Gradle stops complaining.
extra.set("systemProp.javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl")
extra.set("systemProp.javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl")
extra.set("systemProp.javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl")

val mcData = MCData.from(project)
extensions.create("toolkitLegacyFabric", LegacyFabricExtension::class)
setupLoom(mcData) {
    if (mcData.isLegacyFabric) {
        setIntermediateMappingsProvider(LegacyFabricIntermediaryMappingsProvider::class.java) {
            configure(project)
        }
    }
}

plugins {
    id("dev.deftu.gradle.loom")
}

val extension = extensions.create("toolkitLoomHelper", LoomHelperExtension::class)

loom {
    runConfigs {
        all {
            isIdeConfigGenerated = true
        }
    }
}

repositories {
    if (mcData.isNeoForge) {
        maven("https://maven.neoforged.net/releases")
    }

    if (mcData.isLegacyFabric) {
        maven("https://maven.legacyfabric.net/")
    }
}

dependencies {
    if (propertyBoolOr("loom.minecraft.setup", true)) {
        minecraft(propertyOr("loom.minecraft", "com.mojang:minecraft:${mcData.version}"))
    }

    if (propertyBoolOr("loom.mappings.use", true)) {
        fun Dependency?.applyExclusions() {
            check(this != null && this is ModuleDependency)
            exclude(module = "fabric-loader")
        }

        /**
         * A pair of mappings used for the given environment.
         *
         * The first value is the mappings string, and the second value is whether these should be forced despite the requested configuration.
         */
        val defaultMappings: Pair<String, Boolean> = when {
            mcData.isLegacyFabric -> "net.legacyfabric:yarn:${mcData.dependencies.legacyFabric.legacyYarnVersion}" to false
            mcData.isFabric -> "net.fabricmc:yarn:${mcData.dependencies.fabric.yarnVersion}" to false
            mcData.isForge && mcData.version <= MinecraftVersions.VERSION_1_15_2 -> mcData.dependencies.forge.mcpDependency to true
            else -> "official" to false
        }

        val mappingsNotation = if (defaultMappings.second) defaultMappings.first else propertyOr(
            "loom.mappings",
            defaultMappings.first
        )

        val mappingsFlavor = if (defaultMappings.second) "" else propertyOr("loom.mappings.flavor", "")

        if (mappingsFlavor.isNotEmpty()) {
            if (ToolkitConstants.debug) {
                logger.lifecycle("Using mappings flavor: $mappingsFlavor")
            }

            mappings(loom.layered {
                when(mappingsNotation) {
                    "official", "mojang", "mojmap" -> officialMojangMappings()

                    "official-like" -> {
                        if (mcData.version <= MinecraftVersions.VERSION_1_12_2) {
                            if (mcData.isForge) {
                                mappings(mcData.dependencies.forge.mcpDependency)
                            } else {
                                repositories {
                                    maven("https://raw.githubusercontent.com/BleachDev/cursed-mappings/main/")
                                }

                                mappings("net.legacyfabric:yarn:${mcData.version}+build.mcp")
                            }
                        } else officialMojangMappings()
                    }

                    else -> mappings(mappingsNotation)
                }

                when(mappingsFlavor) {
                    "parchment" -> {
                        if (mcData.version >= MinecraftVersions.VERSION_1_16_5) {
                            repositories {
                                maven("https://maven.parchmentmc.org")
                            }

                            parchment("org.parchmentmc.data:parchment-${MinecraftInfo.get(project).getParchmentVersion(mcData.version)}@zip")
                        }
                    }
                }
            }).applyExclusions()
        } else {
            mappings(when(mappingsNotation) {
                "official", "mojang", "mojmap" -> loom.officialMojangMappings()

                "official-like" -> {
                    if (mcData.version <= MinecraftVersions.VERSION_1_12_2) {
                        if (mcData.isForge) {
                            mcData.dependencies.forge.mcpDependency
                        } else {
                            repositories {
                                maven("https://raw.githubusercontent.com/BleachDev/cursed-mappings/main/")
                            }

                            "net.legacyfabric:yarn:${mcData.version}+build.mcp"
                        }
                    } else loom.officialMojangMappings()
                }

                else -> mappingsNotation
            }).applyExclusions()
        }
    }

    if (propertyBoolOr("loom.loader.use", true)) {
        when {
            mcData.isForge -> {
                "forge"("net.minecraftforge:forge:${mcData.dependencies.forge.forgeVersion}")
                loom.forge.pack200Provider.set(Pack200Adapter())
            }

            mcData.isFabric -> modImplementation("net.fabricmc:fabric-loader:${mcData.dependencies.fabric.fabricLoaderVersion}")
            mcData.isNeoForge -> "neoForge"("net.neoforged:neoforge:${mcData.dependencies.neoForged.neoForgeVersion}")
        }
    }
}

// https://github.com/architectury/architectury-loom/pull/10
if (mcData.isModLauncher) {
    (repositories.find { repo ->
        repo.name.contains("Forge", ignoreCase = true)
    } as? MavenArtifactRepository)?.metadataSources {
        mavenPom()
        artifact()
        ignoreGradleMetadataRedirection()
    }
}

if (mcData.isLegacyForge) {
    tasks {
        fun Jar.applyExclusions() {
            exclude("META-INF/versions/**")
            exclude("META-INF/proguard/**")
            exclude("META-INF/maven/**")
            exclude("META-INF/com.android.tools/**")
        }

        named<Jar>("jar") {
            applyExclusions()
        }

        if (isShadowPluginPresent) {
            named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("fatJar") {
                applyExclusions()
            }
        }
    }
}

if (propertyBoolOr("loom.appleSiliconFix", true) && mcData.version < MinecraftVersions.VERSION_1_13) {
    if (
        System.getProperty("os.arch") == "aarch64" &&
        System.getProperty("os.name") == "Mac OS X"
    ) {
        logger.error("Setting up fix with Apple Silicon for Minecraft ${mcData.version}")

        repositories {
            maven("https://maven.legacyfabric.net/") {
                content {
                    includeGroup("org.lwjgl.lwjgl")
                }
            }
        }

        val lwjglVersion = propertyOr("loom.appleSiliconFix.version", "2.9.4+legacyfabric.15")

        configurations.all {
            resolutionStrategy {
                dependencySubstitution {
                    all {
                        if (requested is ModuleComponentSelector) {
                            val module = (requested as ModuleComponentSelector)
                            if (module.group == "org.lwjgl.lwjgl") {
                                useTarget(module.group + ":" + module.module + ":" + lwjglVersion)
                            }
                        }
                    }
                }
            }
        }
    }
}
