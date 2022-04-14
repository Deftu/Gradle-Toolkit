package xyz.unifycraft.gradle

import org.gradle.api.provider.Property

abstract class LoomConfigExtension {
    abstract val version: Property<String>
    abstract val forge: Property<Boolean>

    init {
        forge.convention(false)
    }
}