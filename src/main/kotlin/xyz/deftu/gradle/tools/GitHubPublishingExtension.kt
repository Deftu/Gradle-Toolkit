package xyz.deftu.gradle.tools

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.bundling.Zip
import xyz.deftu.gradle.utils.VersionType
import java.io.File

abstract class GitHubPublishingExtension(
    val project: Project
) {
    abstract val releaseName: Property<String>
    abstract val version: Property<String>
    abstract val draft: Property<Boolean>
    abstract val versionType: Property<VersionType>
    abstract val automaticallyGenerateReleaseNotes: Property<Boolean>

    abstract val changelog: Property<String>
    abstract val changelogFile: Property<File>

    abstract val owner: Property<String>
    abstract val repository: Property<String>

    abstract val file: Property<Zip>

    abstract val useSourcesJar: Property<Boolean>
    abstract val sourcesJar: Property<Zip>
    abstract val useJavadocJar: Property<Boolean>
    abstract val javadocJar: Property<Zip>

    abstract val minecraft: GitHubPublishingMinecraftExtension
        @Nested get

    init {
        draft.convention(false)
        automaticallyGenerateReleaseNotes.convention(false)
        changelog.convention("")
        useSourcesJar.convention(false)
        useJavadocJar.convention(false)
    }

    fun minecraft(action: Action<GitHubPublishingMinecraftExtension>) = action.execute(minecraft)
}

abstract class GitHubPublishingMinecraftExtension {
    abstract val describeFabricWithQuilt: Property<Boolean>

    init {
        describeFabricWithQuilt.convention(false)
    }
}
