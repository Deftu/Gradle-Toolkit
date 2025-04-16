@file:Suppress("ConvertSecondaryConstructorToPrimary")

package dev.deftu.gradle.tools.minecraft

import dev.deftu.gradle.utils.*
import dev.deftu.gradle.utils.version.MinecraftVersion
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.bundling.Zip
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.domainObjectContainer
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import org.jetbrains.annotations.ApiStatus
import java.nio.charset.StandardCharsets
import javax.inject.Inject

abstract class ReleasingV2Extension @Inject constructor(private val project: Project, private val objects: ObjectFactory) {

    abstract val debugMode: Property<Boolean>

    abstract val releaseName: Property<String>

    abstract val releaseVersion: Property<String>

    abstract val gameVersions: ListProperty<MinecraftVersion<*>>

    abstract val loaders: ListProperty<ModLoader>

    val versionType: Property<VersionType> = this.objects.property()

    val file: Property<Zip> = this.objects.property()

    val detectVersionType: Property<Boolean> = this.objects.property()

    val changelog: ChangelogExtension = this.objects.newInstance()

    val tokens: TokenExtension = this.objects.newInstance()

    val jars: JarsExtension = this.objects.newInstance()

    val projectIds: ProjectIdExtension = this.objects.newInstance()

    val dependencies = this.objects.domainObjectContainer(ModDependency::class) { name ->
        this.objects.newInstance(ModDependency::class, name)
    }

    init {
        this.debugMode.convention(false)
        this.releaseName.convention(defaultReleaseName())
        this.releaseVersion.convention(defaultReleaseVersion())
        this.gameVersions.convention(defaultGameVersions())
        this.loaders.convention(defaultLoaders())
        this.versionType.convention(VersionType.RELEASE)
        this.file.convention(project.tasks.named<RemapJarTask>("remapJar").get())
        this.detectVersionType.convention(false)
    }

    fun projectIds(action: Action<in ProjectIdExtension>) {
        action.execute(this.projectIds)
    }

    fun mod(name: String, type: DependencyType = DependencyType.OPTIONAL, action: Action<in ModDependency> = Action {  }) {
        val mod = this.objects.newInstance(ModDependency::class, name).apply {
            this.type.set(type)
        }

        action.execute(mod)
        this.dependencies.add(mod)
    }

    fun mod(name: String, action: Action<in ModDependency> = Action {  }) {
        this.mod(name, DependencyType.OPTIONAL, action)
    }

    private fun defaultReleaseName(): String {
        val mcData = MCData.from(project)
        val modData = ModData.from(project)

        val prefix = buildString {
            var content = ""
            if (project.isMultiversionProject()) {
                content += buildString {
                    append(mcData.loader.friendlyName).append(" ").append(mcData.version)
                }
            }

            if (content.isNotBlank()) {
                append("[").append(content).append("] ")
            }
        }

        return "${prefix}${modData.name} ${modData.version}"
    }

    private fun defaultReleaseVersion(): String {
        val mcData = MCData.from(project)
        val modData = ModData.from(project)
        val gitData = GitData.from(project)

        val version = modData.version
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

            if (project.isMultiversionProject()) {
                content += buildString {
                    if (includingGitData) {
                        append("-")
                    }

                    append(mcData.version)
                    append("-")
                    append(mcData.loader.friendlyString)
                }
            }

            if (content.isNotBlank()) {
                if (!version.endsWith("+")) {
                    append("+")
                }

                append(content)
            }
        }

        return "${version}${suffix}"
    }

    private fun defaultGameVersions(): List<MinecraftVersion<*>> {
        val mcData = MCData.from(project)
        return listOf(mcData.version)
    }

    private fun defaultLoaders(): List<ModLoader> {
        val mcData = MCData.from(project)
        return listOf(mcData.loader)
    }

}

abstract class ChangelogExtension @Inject constructor(objects: ObjectFactory) {

    val content: Property<String> = objects.property()

    val type: Property<ChangelogType> = objects.property()

    init {
        content.convention("No changelog provided.")
        type.convention(ChangelogType.TEXT)
    }

    fun content(content: String) {
        this.content.set(content)
    }

    fun from(file: RegularFile) {
        val javaFile = file.asFile
        if (!javaFile.exists()) {
            return
        }

        this.content(javaFile.readText(StandardCharsets.UTF_8))
    }

    enum class ChangelogType {
        TEXT,
        MARKDOWN
    }

}

abstract class TokenExtension @Inject constructor(project: Project, objects: ObjectFactory) {

    val modrinthToken: Property<String> = objects.property()

    val curseForgeToken: Property<String> = objects.property()

    init {
        modrinthToken.convention(project.propertyOr("publish.modrinth.token", ""))
        curseForgeToken.convention(project.propertyOr("publish.curseforge.apikey", ""))
    }

}

abstract class JarsExtension @Inject constructor(val project: Project) {

    abstract val sourcesJar: Property<Zip>

    abstract val includeSourcesJar: Property<Boolean>

    abstract val javadocJar: Property<Zip>

    abstract val includeJavadocJar: Property<Boolean>

    val isUsingSourcesJar: Boolean
        get() = includeSourcesJar.getOrElse(false) && project.tasks.findByName("sourcesJar").let { it != null && it.enabled }

    val isUsingJavadocJar: Boolean
        get() = includeJavadocJar.getOrElse(false) && project.tasks.findByName("javadocJar").let { it != null && it.enabled }

    val uploadedSourcesJar: Zip
        get() = sourcesJar.orElse(project.getSourcesJarTask()).get()

    val uploadedJavadocJar: Zip
        get() = javadocJar.orElse(project.tasks.named<Jar>("javadocJar")).get()

}

abstract class ProjectIdExtension {

    abstract val modrinth: Property<String>

    abstract val curseforge: Property<String>

}

abstract class ModDependency @Inject constructor(@ApiStatus.Internal val name: String, objects: ObjectFactory) {

    abstract val projectId: Property<String>

    abstract val type: Property<DependencyType>

    val modrinth: ModrinthOverride = objects.newInstance()

    val curseforge: CurseForgeOverride = objects.newInstance()

    private val platformOverrides: MutableMap<Pair<String, String>, DependencyType> = mutableMapOf()

    init {
        type.convention(DependencyType.OPTIONAL)
    }

    fun typeForPlatform(type: DependencyType, vararg versions: String) {
        for (version in versions) {
            platformOverrides["default" to version] = type
        }
    }

    fun getPlatformOverrides(): Map<Pair<String, String>, DependencyType> {
        return platformOverrides.toMap()
    }

}

abstract class ModrinthOverride {
    abstract val projectId: Property<String>
}

abstract class CurseForgeOverride {
    abstract val projectId: Property<String>
}
