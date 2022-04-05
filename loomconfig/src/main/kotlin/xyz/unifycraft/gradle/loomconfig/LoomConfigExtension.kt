package xyz.unifycraft.gradle.loomconfig

import org.gradle.api.provider.Property

abstract class LoomConfigExtension {
    abstract val version: Property<String>
    abstract val side: Property<Side>
    abstract val forge: Property<Boolean>

    init {
        side.convention(Side.CLIENT)
        forge.convention(false)
    }
}