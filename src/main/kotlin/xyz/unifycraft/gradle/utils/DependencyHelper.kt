package xyz.unifycraft.gradle.utils

import java.net.URL

object DependencyHelper {
    fun fetchLatestRelease(repository: String, group: String, artifact: String): String {
        val url = "$repository/${group.replace('.', '/')}/$artifact/maven-metadata.xml"
        val response = URL(url).readText()
        val regex = Regex("<release>(.*?)</release>")
        val match = regex.find(response)
        return match!!.groupValues[1]
    }

    fun fetchLatestRelease(repository: String, dependency: String): String {
        val group = dependency.split(":")
        if (group.size != 2) throw IllegalArgumentException("Dependency must be in format group:artifact")
        return fetchLatestRelease(repository, group[0], group[1])
    }
}
