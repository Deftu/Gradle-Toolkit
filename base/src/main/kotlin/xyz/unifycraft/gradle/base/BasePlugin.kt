package xyz.unifycraft.gradle.base

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class BasePlugin(
    private val requiresEvaluation: Boolean = false
) : Plugin<Project> {
    abstract fun onApply(project: Project)
    override fun apply(project: Project) {
        if (!requiresEvaluation) onApply(project)
        else project.afterEvaluate(::onApply)
    }
}