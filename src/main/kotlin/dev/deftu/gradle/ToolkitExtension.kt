package dev.deftu.gradle

import dev.deftu.gradle.utils.Constants
import org.gradle.api.Project
import dev.deftu.gradle.utils.DependencyHelper
import org.jetbrains.kotlin.gradle.utils.projectCacheDir
import java.io.File

@Suppress("unused")
abstract class ToolkitExtension(
    val project: Project
) {
    fun useEssential() {
        val repo = "https://repo.essential.gg/repository/maven-public"
        project.repositories.maven {
            url = project.uri(repo)
        }

        val mcData = MCData.from(project)
        val loaderDependency = "gg.essential:" + if (mcData.isForge) "loader-launchwrapper" else "loader-fabric"

        val cacheDir = File(project.gradle.projectCacheDir, ".essential-version-cache").apply { mkdirs() }
        val globalCacheDir = File(Constants.dir, ".essential-version-cache").apply { mkdirs() }

        val cachedLoaderFilename = "${mcData.versionStr}-${mcData.loader.name}-LOADER.txt"
        val loaderVersion = DependencyHelper.fetchLatestReleaseOrCached(repo, loaderDependency, cacheDir.resolve(cachedLoaderFilename)) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repo, loaderDependency, globalCacheDir.resolve(cachedLoaderFilename)) ?:
            throw IllegalStateException("Failed to fetch latest Essential loader version.")

        if (mcData.isFabric) {
            // JiJ (Jar-in-Jar) the loader
            project.dependencies.add("include", "$loaderDependency:$loaderVersion")
        } else {
            // Embed the loader
            val usingShadow = project.pluginManager.hasPlugin("dev.deftu.gradle.tools.shadow")
            project.dependencies.add(if (usingShadow) "shade" else "implementation", "$loaderDependency:$loaderVersion")
            if (!usingShadow) project.logger.warn("It is recommended to use DGT Shadow to embed the Essential loader inside your built mod JAR.")
        }

        val cachedApiFilename = "${mcData.versionStr}-${mcData.loader.name}-API.txt"
        val apiDependency = "gg.essential:essential-${mcData.versionStr}-${mcData.loader.name}"
        val apiVersion = DependencyHelper.fetchLatestReleaseOrCached(repo, apiDependency, cacheDir.resolve(cachedApiFilename)) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repo, apiDependency, globalCacheDir.resolve(cachedApiFilename)) ?:
            throw IllegalStateException("Failed to fetch latest Essential API version.")
        project.dependencies.add("compileOnly", "$apiDependency:$apiVersion")
    }

    fun useDevAuth() {
        val repo = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1"
        project.repositories.maven {
            url = project.uri(repo)
        }

        val mcData = MCData.from(project)

        val cacheDir = File(project.gradle.projectCacheDir, ".devauth-version-cache").apply { mkdirs() }
        val globalCacheDir = File(Constants.dir, ".devauth-version-cache").apply { mkdirs() }

        val module = if (mcData.isFabric) "fabric" else if (mcData.isForge && mcData.version <= 11202) "forge-legacy" else "forge-latest"
        val dependency = "me.djtheredstoner:DevAuth-$module"
        val version = DependencyHelper.fetchLatestReleaseOrCached(repo, dependency, cacheDir.resolve("$module.txt")) ?:
            DependencyHelper.fetchLatestReleaseOrCached(repo, dependency, globalCacheDir.resolve("$module.txt")) ?:
            throw IllegalStateException("Failed to fetch latest DevAuth version.")
        project.dependencies.add("modRuntimeOnly", "$dependency:$version")
    }
}
