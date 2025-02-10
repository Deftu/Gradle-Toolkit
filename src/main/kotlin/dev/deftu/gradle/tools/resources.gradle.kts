package dev.deftu.gradle.tools

import dev.deftu.gradle.tools.minecraft.LoomHelperExtension
import dev.deftu.gradle.ToolkitConstants
import dev.deftu.gradle.utils.*

plugins {
    java
}

afterEvaluate {
    val loomHelperExtension = extensions.findByType<LoomHelperExtension>()
    tasks.processResources {
        val mcData = MCData.from(project)
        val projectData = ProjectData.from(project)
        val modData = ModData.from(project)

        val forgeLoaderVersion: String? = run {
            if (!mcData.isPresent) {
                if (ToolkitConstants.debug) logger.warn("Forge loader version not provided for ${project.name} because MCData is not present!")
                return@run null
            }

            if (!mcData.isForgeLike) {
                if (ToolkitConstants.debug) logger.warn("Forge loader version not provided for ${project.name} because a Forge-like loading is not being used!")
                return@run null
            }

            if (mcData.isLegacyForge) {
                if (ToolkitConstants.debug) logger.warn("Forge loader version not provided for ${project.name} because legacy Forge is being used!")
                return@run null
            }

            if (loomHelperExtension === null) {
                if (ToolkitConstants.debug) logger.warn("Forge loader version not provided for ${project.name} because LoomHelperExtension is not present!")
                return@run null
            }

            if (!loomHelperExtension.usingKotlinForForge) {
                if (ToolkitConstants.debug) logger.warn("Forge loader version not provided for ${project.name} because KotlinForForge is not being used!")
                return@run null
            }

            val version = MinecraftInfo.ForgeLike.getKotlinForForgeVersion(mcData.version)
            val majorVersion = version.split(".")[0]
            "[$majorVersion,)"
        }

        if (mcData.isPresent) {
            inputs.property("mc_version", mcData.version.toString())
            inputs.property("patchless_mc_version", mcData.version.patchless)
            inputs.property("padded_mc_version", mcData.version.rawVersion.toString())
            inputs.property("java_version", mcData.version.javaVersionString)
        }

        if (modData.isPresent) {
            inputs.property("mod_version", modData.version)
            inputs.property("mod_id", modData.id)
            inputs.property("mod_name", modData.name)
            inputs.property("mod_group", modData.group)
            inputs.property("mod_description", modData.description)
            inputs.property("file.jarVersion", modData.version.let { if (it[0].isDigit()) it else "0.$it" })
            if (forgeLoaderVersion != null) inputs.property("forge_loader_version", forgeLoaderVersion)
        }

        if (projectData.isPresent) {
            inputs.property("project_version", projectData.version)
            inputs.property("project_group", projectData.group)
            inputs.property("project_name", projectData.name)
            inputs.property("project_description", projectData.description)
        }

        filesMatching(
            listOf(
                "**/mcmod.info",
                "**/fabric.mod.json",
                "**/quilt.mod.json",
                "**/META-INF/mods.toml",
                "**/META-INF/neoforge.mods.toml",
                "**/mixins.*.json",
                "**/*.mixins.json"
            )
        ) {
            expand(mutableMapOf<String, String>().apply {
                if (mcData.isPresent) {
                    put("mc_version", mcData.version.toString())
                    put("minor_mc_version", mcData.version.patchless)
                    put("format_mc_version", mcData.version.rawVersion.toString())
                    put("java_version", mcData.version.javaVersionString)
                }

                if (modData.isPresent) {
                    put("mod_version", modData.version)
                    put("mod_id", modData.id)
                    put("mod_name", modData.name)
                    put("mod_group", modData.group)
                    put("mod_description", modData.description)
                    put("file.jarVersion", modData.version.let { if (it[0].isDigit()) it else "0.$it" })
                    if (forgeLoaderVersion != null) put("forge_loader_version", forgeLoaderVersion)
                }
            })
        }

        filesMatching(
            listOf(
                "*.txt",
                "*.json",
                "*.properties",
                "*.md",
                "*.yml",
                "*.yaml",
                "*.toml",
                "*.xml",
                "*.info",
                "*.cfg",
                "*.conf",
                "*.config",
                "*.lang"
            )
        ) {
            expand(mutableMapOf<String, Any>().apply {
                if (projectData.isPresent) {
                    put("project_version", projectData.version)
                    put("project_group", projectData.group)
                    put("project_name", projectData.name)
                    put("project_description", projectData.description)
                }
            })
        }

        // Only exclude our FMJ if we're NOT using Fabric
        if (!mcData.isFabric) {
            exclude("fabric.mod.json")
        }

        // Only exclude our mods.toml if we're NOT on Forge 1.14+ (ModLauncher) or we're NOT on NeoForge for 1.20.4 and below
        if (!mcData.isModLauncher && (mcData.isNeoForge && mcData.version > MinecraftVersion.VERSION_1_20_4)) {
            exclude("META-INF/mods.toml")
        }

        // Only exclude our neoforge.mods.toml if we're NOT using NeoForge for 1.20.6 and above
        if (!mcData.isNeoForge || mcData.version < MinecraftVersion.VERSION_1_20_6) {
            exclude("META-INF/neoforge.mods.toml")
        }

        // Only exclude our mcmod.info if we're NOT using Forge <=1.12.2
        if (!mcData.isLegacyForge) {
            exclude("mcmod.info")
        }
    }
}
