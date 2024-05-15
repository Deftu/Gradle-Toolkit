package dev.deftu.gradle.tools

import dev.deftu.gradle.ProjectData
import gradle.kotlin.dsl.accessors._8eee71391307bc564dab028e1f37d739.application

plugins {
    application
}

val projectData = ProjectData.from(project)

application {
    if (projectData.mainClass.isBlank())
        throw IllegalArgumentException("No main class specified! This is required to use the `application` plugin.")

    mainClass.set(projectData.mainClass)
}
