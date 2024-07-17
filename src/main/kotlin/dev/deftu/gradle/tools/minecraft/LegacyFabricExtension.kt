package dev.deftu.gradle.tools.minecraft

import org.gradle.api.provider.Property

abstract class LegacyFabricExtension {

    abstract val intermediaryVersion: Property<Int>

    init {
        intermediaryVersion.convention(1)
    }

}
