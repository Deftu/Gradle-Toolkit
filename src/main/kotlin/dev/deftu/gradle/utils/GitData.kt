package dev.deftu.gradle.utils

import dev.deftu.gradle.ToolkitConstants
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.io.OutputStream

data class GitData(
    val present: Boolean,
    val branch: String,
    val commit: String,
    val url: String
) {

    fun shouldAppendVersion(project: Project): Boolean {
        return present && project.propertyBoolOr("gitdata.version", default = false, prefix = false)
    }

    companion object {

        private val debug: Boolean
            get() = ToolkitConstants.debug || System.getProperty("dgt.debug.git", "false").toBoolean()

        private val errorOutput: OutputStream?
            get() = if (debug) System.err else ByteArrayOutputStream()

        @JvmStatic
        fun from(project: Project): GitData {
            val extension = project.extensions.findByName("gitData") as GitData?
            if (extension != null) return extension

            val branch = project.propertyOr("GITHUB_REF_NAME", fetchCurrentBranch(project), false)
            val commit = project.propertyOr("GITHUB_SHA", fetchCurrentCommit(project), false)
            val url = fetchCurrentUrl(project) ?: "NONE"
            val data = GitData(branch.isNotBlank() && commit.isNotBlank(), branch, commit, url)
            project.extensions.add("gitData", data)
            return data
        }

        private fun setupExecute(project: Project, vararg command: String): ByteArrayOutputStream {
            val output = ByteArrayOutputStream()
            project.exec {
                commandLine(*command)
                isIgnoreExitValue = true
                standardOutput = output
                errorOutput = Companion.errorOutput
            }

            return output
        }

        @JvmStatic
        fun fetchCurrentBranch(project: Project): String {
            return try {
                val output = setupExecute(project, "git", "rev-parse", "--abbrev-ref", "HEAD")
                val string = output.toString().trim()
                if (string.isEmpty() || string.startsWith("fatal")) "" else string
            } catch (e: Exception) {
                project.logger.error("Failed to fetch git branch", e)

                ""
            }
        }

        @JvmStatic
        fun fetchCurrentCommit(project: Project): String {
            return try {
                val output = setupExecute(project, "git", "rev-parse", "HEAD")
                val string = output.toString().trim()
                if (string.isEmpty() || string.startsWith("fatal")) "" else string.substring(0, 7)
            } catch (e: Exception) {
                project.logger.error("Failed to fetch git commit", e)

                ""
            }
        }

        @JvmStatic
        fun fetchCurrentUrl(project: Project): String? {
            return try {
                val output = setupExecute(project, "git", "config", "--get", "remote.origin.url")
                val string = output.toString().trim()
                if (string.isEmpty() || string.startsWith("fatal")) "" else string
            } catch (e: Exception) {
                project.logger.error("Failed to fetch git url", e)

                ""
            }
        }

    }
}
