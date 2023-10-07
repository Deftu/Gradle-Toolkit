package dev.deftu.gradle.utils

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
}
