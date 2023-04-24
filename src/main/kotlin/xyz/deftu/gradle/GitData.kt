package xyz.deftu.gradle

import org.gradle.api.Project
import xyz.deftu.gradle.utils.Constants
import xyz.deftu.gradle.utils.propertyBoolOr
import xyz.deftu.gradle.utils.propertyOr
import java.io.ByteArrayOutputStream
import java.io.OutputStream

data class GitData(
    val present: Boolean,
    val branch: String,
    val commit: String,
    val url: String
) {
    fun shouldAppendVersion(project: Project): Boolean {
        return present && project.propertyBoolOr("gitdata.version", false)
    }

    companion object {
        private val debug: Boolean
            get() = Constants.debug || System.getProperty("dgt.debug.git", "false").toBoolean()
        private val errorOutput: OutputStream?
            get() = if (debug) System.err else ByteArrayOutputStream()

        val ciBuild: Boolean
            get() = System.getenv("GITHUB_ACTIONS") == "true"

        @JvmStatic
        fun transformVersion(project: Project, version: String): String {
            val shouldTransform = project.propertyBoolOr("git.version", true)
            if (!shouldTransform && !ciBuild) return version

            val gitData = from(project)
            if (!gitData.present) return version

            return "$version+${gitData.branch}-${gitData.commit}"
        }

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
                commandLine(command)
                isIgnoreExitValue = true
                standardOutput = output
                errorOutput = this@Companion.errorOutput
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
                ""
            }
        }
    }
}
