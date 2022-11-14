package xyz.deftu.gradle.tools

import xyz.deftu.gradle.MCData
import xyz.deftu.gradle.ModData
import xyz.deftu.gradle.utils.isLoomPresent
import xyz.deftu.gradle.utils.propertyBoolOr

plugins {
    java
}

val mcData = MCData.from(project)
val modData = ModData.from(project)

if (propertyBoolOr("mod.version.setup", true))
    version = modData.version
if (propertyBoolOr("mod.group.setup", true))
    group = modData.group
if (propertyBoolOr("mod.name.setup", true)) {
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
