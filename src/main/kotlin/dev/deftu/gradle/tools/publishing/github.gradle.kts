package dev.deftu.gradle.tools.publishing

import com.github.breadmoirai.githubreleaseplugin.GithubReleaseExtension
import dev.deftu.gradle.ToolkitConstants
import dev.deftu.gradle.utils.*
import java.nio.charset.StandardCharsets
import java.util.*

plugins {
    id("com.github.breadmoirai.github-release")
}

val taskName = "publishProjectToGitHub"

val gitData = GitData.from(project)
val mcData = MCData.from(project)
val projectData = ProjectData.from(project)
val modData = ModData.from(project)
val extension = extensions.create("toolkitGitHubPublishing", GitHubPublishingExtension::class)

fun GitHubPublishingExtension.getReleaseName(): String {
    val configuredReleaseName = releaseName.orNull
    if (!configuredReleaseName.isNullOrBlank()) return configuredReleaseName

    val prefix = buildString {
        var content = ""
        if (isMultiversionProject()) {
            content += buildString {
                if (mcData.isFabric && minecraft.describeFabricWithQuilt.get()) {
                    append("Fabric/Quilt")
                } else {
                    append(mcData.loader.friendlyName)
                }

                append(" ").append(mcData.version)
            }
        }

        if (content.isNotBlank()) {
            append("[").append(content).append("] ")
        }
    }

    val backupName = if (modData.isPresent) modData.name else projectData.name
    val backupVersion = if (modData.isPresent) modData.version else projectData.version
    return "$prefix$backupName $backupVersion"
}

fun GitHubPublishingExtension.getReleaseVersion(): String {
    val suffix = buildString {
        var content = ""

        val includingGitData = gitData.shouldAppendVersion(project)
        if (includingGitData) {
            content += buildString {
                append(gitData.branch)
                append("-")
                append(gitData.commit)
            }
        }

        if (isMultiversionProject()) {
            content += buildString {
                if (includingGitData) append("+")
                append(mcData.version)
                append("-")
                append(mcData.loader.friendlyString)
            }
        }

        if (content.isNotBlank()) {
            append("+")
            append(content)
        }
    }

    val backupVersion = if (modData.isPresent) modData.version else projectData.version
    return "${version.getOrElse(backupVersion)}${suffix}"
}

fun GitHubPublishingExtension.getUploadFile(): Zip {
    val backupJar = if (modData.isPresent)
        tasks.named<org.gradle.jvm.tasks.Jar>("remapJar").get()
    else tasks.named<org.gradle.jvm.tasks.Jar>("jar").get()
    return file.getOrElse(backupJar)
}

fun GitHubPublishingExtension.shouldAddSourcesJar() = useSourcesJar.getOrElse(false) && tasks.findByName("sourcesJar") != null
fun GitHubPublishingExtension.shouldAddJavadocJar() = useJavadocJar.getOrElse(false) && tasks.findByName("javadocJar") != null

fun GitHubPublishingExtension.getSourcesJar(): Zip = sourcesJar.getOrElse(getSourcesJarTask().get())
fun GitHubPublishingExtension.getJavadocJar(): Zip = javadocJar.getOrElse(tasks.named<Jar>("javadocJar").get())

fun getGitHubToken(): String? {
    val property = project.findProperty("dgt.publish.github.token")
    return property?.toString() ?: System.getenv("GITHUB_TOKEN")
}

afterEvaluate {
    val token = getGitHubToken()
    if (token.isNullOrBlank()) return@afterEvaluate

    if (extension.changelogFile.isPresent) {
        val changelogFile = extension.changelogFile.get()
        logger.info("Setting GitHub publishing changelog to contents of ${changelogFile.name}")
        val changelog = changelogFile.readText(StandardCharsets.UTF_8)
        extension.changelog.set(changelog)
    }

    val owner = extension.owner.orNull
    val repository = extension.repository.orNull
    if (owner.isNullOrBlank() || repository.isNullOrBlank()) return@afterEvaluate

    configure<GithubReleaseExtension> {
        setToken(token)

        this.owner.set(owner)
        this.repo.set(repository)

        releaseName.set(extension.getReleaseName())
        tagName.set(extension.getReleaseVersion())
        draft.set(extension.draft.getOrElse(false))
        prerelease.set(extension.versionType.getOrElse(VersionType.RELEASE) != VersionType.RELEASE)
        generateReleaseNotes.set(extension.automaticallyGenerateReleaseNotes.getOrElse(false))
        body.set(extension.changelog.get())
        if (extension.targetCommitish.isPresent) targetCommitish.set(extension.targetCommitish.get())

        val usedAssets = mutableListOf<Zip>()
        usedAssets.add(extension.getUploadFile())

        if (extension.shouldAddSourcesJar()) usedAssets.add(extension.getSourcesJar())
        if (extension.shouldAddJavadocJar()) usedAssets.add(extension.getJavadocJar())

        releaseAssets(*usedAssets.toTypedArray())
    }

    tasks.register(taskName) {
        group = ToolkitConstants.TASK_GROUP

        dependsOn("build", "githubRelease")
    }
}
