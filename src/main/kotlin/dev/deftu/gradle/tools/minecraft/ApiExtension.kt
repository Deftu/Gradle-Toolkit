package dev.deftu.gradle.tools.minecraft

import dev.deftu.gradle.ToolkitConstants
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import dev.deftu.gradle.utils.withLoom
import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.sourceSets

abstract class ApiExtension(
    val project: Project
) {
    companion object {
        const val SOURCESET_NAME = "testMod"
    }

    private fun getTestSourceset() = project.sourceSets.findByName(SOURCESET_NAME)

    fun setupTestSourceset() {
        val current = getTestSourceset()
        if (current != null) return

        val mainSourceset = project.sourceSets["main"]
        if (mainSourceset == null) {
            project.logger.warn("Main sourceset not found, skipping test sourceset setup")
            return
        }

        project.sourceSets.create(SOURCESET_NAME) {
            compileClasspath += mainSourceset.compileClasspath
            runtimeClasspath += mainSourceset.runtimeClasspath
        }

        project.dependencies {
            "${SOURCESET_NAME}Implementation"(mainSourceset.output)
            "${SOURCESET_NAME}RuntimeOnly"(mainSourceset.output)
            "${SOURCESET_NAME}CompileOnly"(mainSourceset.output)
        }
    }

    fun setupTestJar() {
        val current = getTestSourceset() ?: throw IllegalStateException("Test sourceset not found")

        val devLibsDir = project.layout.buildDirectory.dir("dev-libs")

        val testJar = project.tasks.register("testJar", Jar::class.java) {
            group = ToolkitConstants.TASK_GROUP

            archiveClassifier.set("test-mod-dev")
            destinationDirectory.set(devLibsDir)
            from(project.sourceSets[SOURCESET_NAME].output)
        }.get()

        val remapTestJar = project.tasks.register("remapTestJar", RemapJarTask::class.java) {
            group = ToolkitConstants.TASK_GROUP

            archiveClassifier.set("test-mod")
            destinationDirectory.set(devLibsDir)
            inputFile.set(testJar.archiveFile)
            classpath.setFrom(current.compileClasspath)
        }.get()

        project.tasks.named("build").configure {
            dependsOn(remapTestJar)
        }
    }

    fun setupTestClient() {
        setupTestSourceset()
        setupTestJar()

        project.withLoom {
            runs {
                create("testClient") {
                    client()
                    source(project.sourceSets[SOURCESET_NAME])
                }
            }
        }
    }

    fun setupTestServer() {
        setupTestSourceset()
        setupTestJar()

        project.withLoom {
            runs {
                create("testServer") {
                    server()
                    source(project.sourceSets[SOURCESET_NAME])
                }
            }
        }
    }

    fun setupTestWorkspace() {
        setupTestClient()
        setupTestServer()
    }
}
