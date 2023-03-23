package xyz.deftu.gradle.tools.minecraft

import gradle.kotlin.dsl.accessors._2e7c48f5c5e053e2f48db1b0d2cf7734.sourceSets
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import xyz.deftu.gradle.utils.withLoom

abstract class ApiExtension(
    val project: Project
) {
    private fun getTestSourceset() = project.sourceSets.findByName("testMod")

    fun setupTestSourceset() {
        val current = getTestSourceset()
        if (current != null) return

        project.sourceSets.create("testMod") {
            compileClasspath += project.sourceSets["main"].compileClasspath
            runtimeClasspath += project.sourceSets["main"].runtimeClasspath
        }

        project.dependencies {
            "testModImplementation"(project.sourceSets["main"].output)
        }
    }

    fun setupTestJar() {
        val current = getTestSourceset() ?: throw IllegalStateException("Test sourceset not found")

        val testJar = project.tasks.register("testJar", Jar::class.java) {
            group = "build"

            archiveClassifier.set("test-mod-dev")
            destinationDirectory.set(project.layout.buildDirectory.dir("dev-libs"))
            from(project.sourceSets["testMod"].output)
        }.get()

        val remapTestJar = project.tasks.register("remapTestJar", RemapJarTask::class.java) {
            group = "build"

            archiveClassifier.set("test-mod")
            input.set(testJar.archiveFile)
            classpath(current.compileClasspath)
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
                    source(project.sourceSets["testMod"])
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
                    source(project.sourceSets["testMod"])
                }
            }
        }
    }

    fun setupTestWorkspace() {
        setupTestClient()
        setupTestServer()
    }
}
