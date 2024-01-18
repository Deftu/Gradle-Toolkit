package dev.deftu.gradle.utils

import java.io.File
import java.net.URL

object DependencyHelper {
    private val latestReleaseRegex = Regex("<latest>(.*?)</latest>")

    fun fetchLatestRelease(repository: String, group: String, artifact: String): String {
        val url = "$repository/${group.replace('.', '/')}/$artifact/maven-metadata.xml"
        val response = URL(url).readText()
        if (Constants.debug) println("DependencyHelper#fetchLatestRelease:\n$response")

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
            val response = URL(url).readText()
            if (Constants.debug) println("DependencyHelper#fetchLatestRelease:\n$response")

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
}
