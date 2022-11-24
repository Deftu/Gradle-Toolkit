package xyz.deftu.gradle

import org.gradle.api.Project
import xyz.deftu.gradle.utils.Constants
import xyz.deftu.gradle.utils.propertyOr
import java.io.ByteArrayOutputStream
import java.io.OutputStream

data class GitData(
    val branch: String,
    val commit: String,
    val url: String
) {
    companion object {
        private val debug: Boolean
            get() = Constants.debug || System.getProperty("dgt.debug.git", "false").toBoolean()
        private val errorOutput: OutputStream?
            get() = if (debug) System.err else ByteArrayOutputStream()

        @JvmStatic
        fun from(project: Project): GitData {
            val extension = project.extensions.findByName("gitData") as GitData?
            if (extension != null) return extension

            val branch = project.propertyOr("GITHUB_REF_NAME", fetchCurrentBranch(project) ?: "LOCAL", false)
            val commit = project.propertyOr("GITHUB_SHA", fetchCurrentCommit(project) ?: "LOCAL", false)
            val url = fetchCurrentUrl(project) ?: "NONE"
            val data = GitData(branch, commit, url)
            project.extensions.add("gitData", data)
            return data
        }

        @JvmStatic
        fun fetchCurrentBranch(project: Project): String? {
            return try {
                val output = ByteArrayOutputStream()
                project.exec {
                    commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
                    standardOutput = output
                    errorOutput = this@Companion.errorOutput
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
                    errorOutput = this@Companion.errorOutput
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
                    errorOutput = this@Companion.errorOutput
                }
                val string = output.toString().trim()
                if (string.isEmpty() || string.startsWith("fatal")) "LOCAL" else string
            } catch (e: Exception) {
                "LOCAL"
            }
        }
    }
}
