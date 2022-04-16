package xyz.unifycraft.gradle.snippets

import com.modrinth.minotaur.dependencies.Dependency
import org.gradle.api.Action
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import org.gradle.jvm.tasks.Jar

abstract class ModPublishingExtension {
    @Nested abstract fun getModrinth(): PublishingModrinthExtension
    fun modrinth(action: Action<PublishingModrinthExtension>) = action.execute(getModrinth())
}

abstract class PublishingModrinthExtension {
    abstract val token: Property<String>
    abstract val projectId: Property<String>
    abstract val version: Property<String>
    abstract val versionType: Property<ModrinthVersionType>
    abstract val uploadFile: Property<Jar>
    abstract val gameVersions: ListProperty<String>
    abstract val loaders: ListProperty<String>
    abstract val dependencies: ListProperty<Dependency>
    init {
        token.convention("")
        projectId.convention("")
        version.convention("")
        versionType.convention(ModrinthVersionType.RELEASE)
        gameVersions.convention(listOf())
        loaders.convention(listOf())
        dependencies.convention(listOf())
    }
}