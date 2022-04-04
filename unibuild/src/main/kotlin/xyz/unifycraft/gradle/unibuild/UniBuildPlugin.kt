package xyz.unifycraft.gradle.unibuild

import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPlugin
import org.gradle.util.GradleVersion
import xyz.unifycraft.gradle.base.BasePlugin
import xyz.unifycraft.gradle.base.utils.CommandLineHelper

class UniBuildPlugin : BasePlugin() {
    override fun onApply(project: Project) {
        val extension = project.extensions.create("unibuild", UniBuildExtension::class.java)
        val version = project.version as String

        val gitBranch = fetchGitBranch(project, extension)
        val gitHash = fetchGitHash(project, extension)
        val buildNumber = fetchBuildNumber(extension)

        val task = project.tasks.register("applyBuildInfo") {
            it.group = "unibuild"
            project.logger.lifecycle("Applying build information to {}.", project.displayName)

            var modifiedArchiveName = false
            if (extension.hasMcInfo() && project.plugins.hasPlugin("base")) {
                val archivesBaseName = fetchArchivesBaseName(project)
                applyArchivesBaseName(project, buildString {
                    append(archivesBaseName)
                    append("-")
                    append(extension.mcVersion.get())
                    append("-")
                    append(extension.mcPlatform.get())
                })
                modifiedArchiveName = true
            }

            project.version = buildString {
                append(version)
                if (gitBranch != null && gitHash.isNotEmpty())
                    append("$gitBranch.$gitHash")
                if (buildNumber.isNotEmpty())
                    append(".$buildNumber")
            }

            project.logger.lifecycle(
                "Build information was applied to {}.\n" +
                        (if (modifiedArchiveName) "Archive name: ${fetchArchivesBaseName(project)}\n" else "") +
                        "Version: {}\n",
                project.displayName,
                project.version
            )
        }

        project.afterEvaluate {
            if (project.plugins.hasPlugin(JavaPlugin::class.java) && extension.dependantJar.getOrElse(false))
                project.tasks.getByName("jar").dependsOn(task)
        }
    }

    private fun fetchArchivesBaseName(project: Project) = (project.extensions.getByName("base") as BasePluginExtension).let {
        if (GradleVersion.current() >= GradleVersion.version("8.0.0"))
            it.archivesName.get()
        else it.archivesBaseName
    } as String
    private fun applyArchivesBaseName(project: Project, value: String) = (project.extensions.getByName("base") as BasePluginExtension).apply {
        if (GradleVersion.current() >= GradleVersion.version("8.0.0"))
            archivesName.set(value)
        else archivesBaseName = value
        project.extensions.extraProperties["buildName"] = value
    }

    companion object {
        fun fetchGitBranch(project: Project, extension: UniBuildExtension): String? {
            return try {
                if (!extension.usesGitBranch()) return ""
                if (extension.gitBranch.isPresent) return extension.gitBranch.get()
                var branch = System.getenv(extension.gitBranchName.getOrElse(UniBuildExtension.GIT_BRANCH_NAME_DEFAULT))
                if (branch == null)
                    branch = CommandLineHelper.fetchCommandOutput(project, "git", "rev-parse", "--abbrev-ref", "HEAD")
                if (branch.isNotEmpty() && !extension.gitBranchExclusions.getOrElse(UniBuildExtension.GIT_BRANCH_EXCLUSIONS_DEFAULT).contains(branch))
                    String.format("-%s", branch) else ""
            } catch (e: Exception) {
                null
            }
        }

        fun fetchGitHash(project: Project, extension: UniBuildExtension): String {
            return try {
                if (!extension.usesGitHash()) return ""
                if (extension.gitHash.isPresent) return extension.gitHash.get()
                var hash = System.getenv(extension.gitHashName.getOrElse(UniBuildExtension.GIT_HASH_NAME_DEFAULT))
                if (hash == null)
                    hash = CommandLineHelper.fetchCommandOutput(project, "git", "log", "-n", "1", "--pretty=tformat:%h")
                hash.ifEmpty { "LOCAL" }
            } catch (e: Exception) {
                ""
            }
        }

        fun fetchBuildNumber(extension: UniBuildExtension): String {
            return try {
                if (!extension.usesBuildNumber()) return ""
                if (extension.buildNumber.isPresent) return extension.buildNumber.get()
                System.getenv(extension.buildNumberName.getOrElse(UniBuildExtension.BUILD_NUMBER_NAME_DEFAULT)).ifEmpty { "" }
            } catch (e: Exception) {
                ""
            }
        }
    }
}