package xyz.enhancedpixel.gradle

import org.gradle.api.Project
import xyz.enhancedpixel.gradle.utils.propertyOr
import java.io.ByteArrayOutputStream

data class GitHubData(
    val branch: String,
    val commit: String,
    val url: String
) {
    companion object {
        @JvmStatic
        fun from(project: Project): GitHubData {
            val extension = project.extensions.findByName("githubData") as GitHubData?
            if (extension != null) return extension

            val branch = project.propertyOr("GITHUB_REF_NAME", fetchCurrentBranch(project) ?: "LOCAL")!!
            val commit = project.propertyOr("GITHUB_SHA", fetchCurrentCommit(project) ?: "LOCAL")!!
            val url = fetchCurrentUrl(project) ?: "NONE"
            val data = GitHubData(branch, commit, url)
            project.extensions.add("githubData", data)
            return data
        }

        @JvmStatic
        fun fetchCurrentBranch(project: Project): String? {
            return try {
                val output = ByteArrayOutputStream()
                project.exec {
                    commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
                    standardOutput = output
                }
                val string = output.toString().trim()
                if (string.isEmpty() || string.startsWith("fatal")) null else string
            } catch (e: Exception) {
                "LOCAL"
            }
        }

        @JvmStatic
        fun fetchCurrentCommit(project: Project): String? {
            return try {
                val output = ByteArrayOutputStream()
                project.exec {
                    commandLine("git", "rev-parse", "HEAD")
                    standardOutput = output
                }
                val string = output.toString().trim()
                if (string.isEmpty() || string.startsWith("fatal")) "LOCAL" else string.substring(0, 7)
            } catch (e: Exception) {
                "LOCAL"
            }
        }

        @JvmStatic
        fun fetchCurrentUrl(project: Project): String? {
            return try {
                val output = ByteArrayOutputStream()
                project.exec {
                    commandLine("git", "config", "--get", "remote.origin.url")
                    standardOutput = output
                }
                val string = output.toString().trim()
                if (string.isEmpty() || string.startsWith("fatal")) "LOCAL" else string
            } catch (e: Exception) {
                "LOCAL"
            }
        }
    }
}
