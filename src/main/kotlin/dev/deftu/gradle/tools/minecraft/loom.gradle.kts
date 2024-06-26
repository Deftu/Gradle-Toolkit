package dev.deftu.gradle.tools.minecraft

import dev.architectury.pack200.java.Pack200Adapter
import org.gradle.kotlin.dsl.dependencies
import dev.deftu.gradle.utils.*
import gradle.kotlin.dsl.accessors._0935894d714bf6b98fac60b9fc45a2f5.loom
import gradle.kotlin.dsl.accessors._0935894d714bf6b98fac60b9fc45a2f5.mappings
import gradle.kotlin.dsl.accessors._0935894d714bf6b98fac60b9fc45a2f5.minecraft
import gradle.kotlin.dsl.accessors._0935894d714bf6b98fac60b9fc45a2f5.modImplementation

// Set XML parsers so Gradle stops complaining.
extra.set("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl")
extra.set("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl")
extra.set("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl")

val mcData = MCData.from(project)
setupLoom(mcData) {
    if (mcData.isLegacyFabric) {
        intermediaryUrl.set("https://maven.legacyfabric.net/net/legacyfabric/intermediary/%1\$s/intermediary-%1\$s-v2.jar")
    }
}

plugins {
    id("gg.essential.loom")
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
        maven("https://repo.legacyfabric.net/repository/legacyfabric/")
    }
}

dependencies {
    if (propertyBoolOr("loom.minecraft.setup", true)) {
        minecraft(propertyOr("loom.minecraft", "com.mojang:minecraft:${mcData.version}"))
    }

    if (propertyBoolOr("loom.mappings.use", true)) {
        propertyOr(
            "loom.mappings", when {
                mcData.isLegacyFabric -> "net.legacyfabric:yarn:${mcData.dependencies.legacyFabric.legacyYarnVersion}"
                mcData.isFabric -> "net.fabricmc:yarn:${mcData.dependencies.fabric.yarnVersion}"
                mcData.isForge && mcData.version <= MinecraftVersion.VERSION_1_15_2  -> mcData.dependencies.forge.mcpDependency
                else -> "official"
            }
        ).apply {
            if (this in setOf("official", "mojang", "mojmap")) {
                mappings(loom.officialMojangMappings())
            } else {
                mappings(this) {
                    exclude(module = "fabric-loader")
                }
            }
        }
    }

    if (propertyBoolOr("loom.loader.use", true)) {
        when {
            mcData.isForge -> {
                "forge"("net.minecraftforge:forge:${mcData.dependencies.forge.forgeVersion}")
                loom.forge.pack200Provider.set(Pack200Adapter())
            }

            mcData.isFabric -> modImplementation("net.fabricmc:fabric-loader:${mcData.dependencies.fabric.fabricLoaderVersion}")
            mcData.isNeoForge -> "neoForge"("net.neoforged:neoforge:${mcData.dependencies.neoForged.neoForgedVersion}")
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

afterEvaluate {
    if (extension.appleSiliconFix.get()) {
        if (
            System.getProperty("os.arch") == "aarch64" &&
            System.getProperty("os.name") == "Mac OS X"
        ) {
            val lwjglVersion = if (mcData.version >= MinecraftVersion.VERSION_1_19) "3.3.1" else "3.3.0"
            val lwjglNatives = "natives-macos-arm64"
            logger.error("Setting up fix with Apple Silicon for Minecraft ${mcData.version} ($lwjglVersion, $lwjglNatives)")

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
