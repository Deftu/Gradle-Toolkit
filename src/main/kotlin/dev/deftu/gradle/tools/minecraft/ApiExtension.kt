package dev.deftu.gradle.tools.minecraft

import dev.deftu.gradle.ToolkitConstants
import dev.deftu.gradle.utils.MCData
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import dev.deftu.gradle.utils.withLoom
import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.sourceSets
import org.gradle.api.tasks.SourceSet

abstract class ApiExtension(
    val project: Project
) {

    companion object {

        const val SOURCE_SET_NAME = "testMod"
        const val CLIENT_RUN_NAME = "testClient"
        const val SERVER_RUN_NAME = "testServer"

    }

    private inline val mcData: MCData
        get() = MCData.from(project)

    private inline val sourceSet: SourceSet?
        get() = project.sourceSets.findByName(SOURCE_SET_NAME)

    fun setupTestSourceSet() {
        if (sourceSet != null) {
            // Don't need to set it up if it already exists
            return
        }

        val mainSourceSet = project.sourceSets["main"]
        if (mainSourceSet == null) {
            project.logger.warn("Main source set not found, skipping test source set setup")
            return
        }

        project.sourceSets.create(SOURCE_SET_NAME) {
            compileClasspath += mainSourceSet.compileClasspath
            runtimeClasspath += mainSourceSet.runtimeClasspath
        }

        project.dependencies {
            "${SOURCE_SET_NAME}Implementation"(mainSourceSet.output)
            "${SOURCE_SET_NAME}RuntimeOnly"(mainSourceSet.output)
            "${SOURCE_SET_NAME}CompileOnly"(mainSourceSet.output)
        }
    }

    fun setupTestJar() {
        val currentSourceSet = sourceSet ?: throw IllegalStateException("Test source set not found")

        val devLibsDir = project.layout.buildDirectory.dir("dev-libs")

        val testJar = project.tasks.register("testJar", Jar::class.java) {
            group = ToolkitConstants.TASK_GROUP

            archiveClassifier.set("test-mod-dev")
            destinationDirectory.set(devLibsDir)
            from(project.sourceSets[SOURCE_SET_NAME].output)
        }.get()

        val remapTestJar = project.tasks.register("remapTestJar", RemapJarTask::class.java) {
            group = ToolkitConstants.TASK_GROUP

            archiveClassifier.set("test-mod")
            destinationDirectory.set(devLibsDir)
            inputFile.set(testJar.archiveFile)
            classpath.setFrom(currentSourceSet.compileClasspath)
        }.get()

        project.tasks.named("build").configure {
            dependsOn(remapTestJar)
        }
    }

    fun setupTestClient() {
        setupTestSourceSet()
        setupTestJar()

        project.withLoom {
            runs {
                create(CLIENT_RUN_NAME) {
                    client()
                    source(sourceSet)

                    val configuration = project.configurations.create(CLIENT_RUN_NAME)
                    if (!mcData.isFabric) {
                        mods {
                            create(SOURCE_SET_NAME) {
                                source(sourceSet)
                                configuration(configuration)
                            }
                        }
                    }
                }
            }
        }
    }

    fun setupTestServer() {
        setupTestSourceSet()
        setupTestJar()

        project.withLoom {
            runs {
                create(SERVER_RUN_NAME) {
                    server()
                    source(sourceSet)

                    val configuration = project.configurations.create(SERVER_RUN_NAME)
                    if (!mcData.isFabric) {
                        mods {
                            create(SOURCE_SET_NAME) {
                                source(sourceSet)
                                configuration(configuration)
                            }
                        }
                    }
                }
            }
        }
    }

    fun setupTestWorkspace() {
        setupTestClient()
        setupTestServer()
    }

}
