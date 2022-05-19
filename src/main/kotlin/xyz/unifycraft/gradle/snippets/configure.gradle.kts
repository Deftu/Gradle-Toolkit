package xyz.unifycraft.gradle.snippets

import xyz.unifycraft.gradle.MCData
import xyz.unifycraft.gradle.ModData
import xyz.unifycraft.gradle.utils.propertyBoolOr

plugins {
    java
}

val mcData = MCData.from(project)
val modData = ModData.from(project)

if (propertyBoolOr("mod.version.set", true))
    version = modData.version
if (propertyBoolOr("mod.group.set", true))
    group = modData.group
if (propertyBoolOr("mod.name.set", true)) {
    tasks {
        named<Jar>("jar") {
            archiveBaseName.set(modData.name)
        }
    }
}
