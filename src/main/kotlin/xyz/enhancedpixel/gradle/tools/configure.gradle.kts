package xyz.enhancedpixel.gradle.tools

import xyz.enhancedpixel.gradle.MCData
import xyz.enhancedpixel.gradle.ModData
import xyz.enhancedpixel.gradle.utils.isLoomPresent
import xyz.enhancedpixel.gradle.utils.propertyBoolOr

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
    base.archivesName.set(modData.name)
    tasks {
        if (isLoomPresent()) {
            named<org.gradle.jvm.tasks.Jar>("remapJar") {
                archiveBaseName.set(modData.name)
            }
        } else {
            named<Jar>("jar") {
                archiveBaseName.set(modData.name)
            }
        }
    }
}
