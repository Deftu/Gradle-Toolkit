package xyz.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._2e7c48f5c5e053e2f48db1b0d2cf7734.application
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
