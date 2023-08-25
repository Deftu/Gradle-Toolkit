package xyz.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._fa7f94d12aa30f8de9950944bf07b9ca.application
import xyz.deftu.gradle.ProjectData

plugins {
    application
}

val projectData = ProjectData.from(project)

application {
    if (projectData.mainClass.isBlank())
        throw IllegalArgumentException("No main class specified! This is required to use the `application` plugin.")

    mainClass.set(projectData.mainClass)
}
