package xyz.unifycraft.gradle

import org.gradle.api.Project
import xyz.unifycraft.gradle.utils.DependencyHelper

abstract class ToolkitExtension(
    val project: Project
) {
    fun useEssential() {
        val repo = "https://repo.essential.gg/repository/maven-releases"
        val mcData = MCData.from(project)
        val loaderDependency = "gg.essential:" + if (mcData.isForge) "loader-launchwrapper" else "loader-fabric"
        val loaderVersion = DependencyHelper.fetchLatestRelease(repo, loaderDependency)
        val usingUnishadow = project.pluginManager.hasPlugin("xyz.unifycraft.gradle.tools.shadow")
        project.dependencies.add(if (usingUnishadow) "unishade" else "implementation", "$loaderDependency:$loaderVersion")
        if (!usingUnishadow) project.logger.warn("> It is recommended to use UCGT Shadow to embed the Essential loader inside your built mod JAR.")
        val apiDependency = "gg.essential:essential-${mcData.versionStr}-${mcData.loader.name}"
        val apiVersion = DependencyHelper.fetchLatestRelease(repo, apiDependency)
        project.dependencies.add("compileOnly", "$apiDependency:$apiVersion")
    }

    fun useDevAuth() {
        val repo = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1"
        project.repositories.maven {
            url = project.uri(repo)
            name = "DJtheRedstoner DevAuth"
        }
        val mcData = MCData.from(project)
        val module = if (mcData.isFabric) "fabric" else if (mcData.isForge && mcData.version <= 11202) "forge-legacy" else "forge-latest"
        val dependency = "me.djtheredstoner:DevAuth-$module"
        val version = DependencyHelper.fetchLatestRelease(repo, dependency)
        project.dependencies.add("runtimeOnly", "$dependency:$version")
    }

    // TODO: useUniCore
}
