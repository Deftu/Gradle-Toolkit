package xyz.unifycraft.gradle

import org.gradle.api.provider.Property

abstract class LoomShadowExtension {
    abstract val autoDepend: Property<Boolean>
    init {
        autoDepend.convention(true)
    }
}