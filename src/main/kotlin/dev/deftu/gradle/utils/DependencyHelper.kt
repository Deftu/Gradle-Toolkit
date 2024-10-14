package dev.deftu.gradle.utils

import dev.deftu.gradle.ToolkitConstants
import org.gradle.api.Project
import java.io.File
import java.net.URI

object DependencyHelper {

    private val latestReleaseRegex = Regex("<latest>(.*?)</latest>")

    fun fetchLatestRelease(repository: String, group: String, artifact: String): String {
        val url = "$repository/${group.replace('.', '/')}/$artifact/maven-metadata.xml"
        val response = URI.create(url).toURL().readText()
        if (ToolkitConstants.debug) {
            println("DependencyHelper#fetchLatestRelease:\n$response")
        }

        val match = latestReleaseRegex.find(response)
        return match!!.groupValues[1]
    }

    fun fetchLatestRelease(repository: String, dependency: String): String {
        val group = dependency.split(":")
        if (group.size != 2) throw IllegalArgumentException("Dependency must be in format group:artifact")
        return fetchLatestRelease(repository, group[0], group[1])
    }

    fun fetchLatestReleaseOrCached(
        repository: String,
        group: String,
        artifact: String,
        file: File
    ): String? {
        val content = try {
            val url = "$repository/${group.replace('.', '/')}/$artifact/maven-metadata.xml"
            val response = URI.create(url).toURL().readText()
            if (ToolkitConstants.debug) {
                println("DependencyHelper#fetchLatestReleaseOrCached:\n$response")
            }

            if (!file.exists()) file.createNewFile()
            file.writeText(response)

            response
        } catch (e: Exception) {
            if (file.exists()) file.readText() else null
        } ?: return null

        val match = latestReleaseRegex.find(content)
        return match!!.groupValues[1]
    }

    fun fetchLatestReleaseOrCached(
        repository: String,
        dependency: String,
        file: File
    ): String? {
        val group = dependency.split(":")
        if (group.size != 2) throw IllegalArgumentException("Dependency must be in format group:artifact")
        return fetchLatestReleaseOrCached(repository, group[0], group[1], file)
    }

    /**
     * Returns the latest version of a dependency which is stored in the Maven local repository.
     */
    fun fetchLatestReleaseFromLocal(project: Project, group: String, artifact: String): String? {
        val mavenLocalPath = project.repositories.mavenLocal().url.toURL().path

        val mavenMetadataLocalFile = File("$mavenLocalPath/${group.replace('.', '/')}/$artifact/maven-metadata-local.xml")
        val content = if (!mavenMetadataLocalFile.exists()) {
            val mavenMetadataFile = File("$mavenLocalPath/${group.replace('.', '/')}/$artifact/maven-metadata.xml")
            if (!mavenMetadataFile.exists()) {
                return null
            }

            mavenMetadataFile.readText()
        } else mavenMetadataLocalFile.readText()

        val match = latestReleaseRegex.find(content)
        return match!!.groupValues[1]
    }

    fun fetchLatestReleaseFromLocal(project: Project, dependency: String): String? {
        val group = dependency.split(":")
        if (group.size != 2) throw IllegalArgumentException("Dependency must be in format group:artifact")
        return fetchLatestReleaseFromLocal(project, group[0], group[1])
    }

}
