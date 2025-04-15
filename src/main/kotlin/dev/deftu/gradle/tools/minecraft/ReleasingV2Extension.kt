@file:Suppress("ConvertSecondaryConstructorToPrimary")

package dev.deftu.gradle.tools.minecraft

import dev.deftu.gradle.utils.DependencyType
import dev.deftu.gradle.utils.ModLoader
import dev.deftu.gradle.utils.VersionType
import dev.deftu.gradle.utils.propertyOr
import dev.deftu.gradle.utils.version.MinecraftVersion
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.domainObjectContainer
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import org.jetbrains.annotations.ApiStatus
import java.nio.charset.StandardCharsets
import javax.inject.Inject

abstract class ReleasingV2Extension @Inject constructor(private val objects: ObjectFactory) {

    abstract val debugMode: Property<Boolean>

    abstract val releaseName: Property<String>

    abstract val releaseVersion: Property<String>

    abstract val gameVersions: ListProperty<MinecraftVersion<*>>

    abstract val loaders: ListProperty<ModLoader>

    val versionType: Property<VersionType> = this.objects.property<VersionType>().convention(VersionType.RELEASE)

    val file: Property<Zip> = this.objects.property()

    val detectVersionType: Property<Boolean> = this.objects.property<Boolean>().convention(false)

    val changelog: ChangelogExtension = this.objects.newInstance()

    val tokens: TokenExtension = this.objects.newInstance()

    val jars: JarsExtension = this.objects.newInstance()

    val projectIds: ProjectIdExtension = this.objects.newInstance()

    val dependencies = this.objects.domainObjectContainer(ModDependency::class) { name ->
        this.objects.newInstance(ModDependency::class, name)
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

}

abstract class ChangelogExtension @Inject constructor(objects: ObjectFactory) {

    val content: Property<String> = objects.property<String>().convention("No changelog provided.")

    val type: Property<ChangelogType> = objects.property<ChangelogType>().convention(ChangelogType.TEXT)

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

    val modrinthToken: Property<String> = objects.property<String>().convention(project.propertyOr("publish.modrinth.token", ""))

    val curseForgeToken: Property<String> = objects.property<String>().convention(project.propertyOr("publish.curseforge.apikey", ""))

}

abstract class JarsExtension {

    abstract val sourcesJar: Property<Zip>

    abstract val includeSourcesJar: Property<Boolean>

    abstract val javadocJar: Property<Zip>

    abstract val includeJavadocJar: Property<Boolean>

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

    fun typeForPlatform(type: DependencyType, vararg versions: String) {
        for (version in versions) {
            platformOverrides["default" to version] = type
        }
    }

    fun getPlatformOverrides(): Map<Pair<String, String>, DependencyType> {
        return platformOverrides
    }

}

abstract class ModrinthOverride {
    abstract val projectId: Property<String>
}

abstract class CurseForgeOverride {
    abstract val projectId: Property<String>
}
