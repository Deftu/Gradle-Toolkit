package xyz.deftu.gradle.tools.minecraft

import gradle.kotlin.dsl.accessors._11d1d69a77e50fb2b4b174f119312f10.implementation
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.repositories
import xyz.deftu.gradle.MCData

val mcData = MCData.from(project)

val setupSpigot by tasks.registering {
    description = "Downloads and sets up a Spigot test server for the current Minecraft version."
    group = "deftu"
    // download latest BuildTools.jar - https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
    // run BuildTools.jar with java -jar BuildTools.jar --rev mcData.versionStr
    // copy the generated jar to a test server folder

    val buildToolsJar = project.buildDir.resolve("BuildTools.jar")
    val serverFolder = project.projectDir.resolve("server")
    val serverJar = serverFolder.resolve("spigot-${mcData.versionStr}.jar")

    doLast {
        if (!buildToolsJar.exists() && !serverJar.exists()) {
            project.logger.lifecycle("Downloading BuildTools...")
            buildToolsJar.parentFile.mkdirs()
            buildToolsJar.createNewFile()
            val url = java.net.URL("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar")
            val connection = url.openConnection()
            val totalSize = connection.contentLengthLong
            var downloadedSize = 0L
            val inputStream = connection.getInputStream()
            val outputStream = buildToolsJar.outputStream()
            val buffer = ByteArray(1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
                downloadedSize += read
                val progress = downloadedSize.toDouble() / totalSize.toDouble()
                if (progress % 0.1 == 0.0) {
                    project.logger.lifecycle("Downloaded ${downloadedSize / 1024} KB of ${totalSize / 1024} KB (${(progress * 100).toInt()}%)")
                }
            }

            outputStream.close()
            inputStream.close()
            project.logger.lifecycle("Downloaded BuildTools.")
        }

        fun copyServerJar() {
            project.logger.lifecycle("Copying server JAR...")
            serverFolder.mkdirs()
            serverJar.createNewFile()
            serverJar.writeBytes(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(project.buildDir.absolutePath, "spigot-${mcData.versionStr}.jar")))
        }

        if (serverJar.exists()) copyServerJar()

        if (!serverJar.exists()) {
            project.logger.lifecycle("Running BuildTools...")
            val process = ProcessBuilder("java", "-jar", buildToolsJar.absolutePath, "--rev", mcData.versionStr).directory(buildToolsJar.parentFile).start()
            process.inputStream.bufferedReader().forEachLine { project.logger.lifecycle("[BuildTools] $it") }
            process.errorStream.bufferedReader().forEachLine { project.logger.error("[BuildTools] $it") }
            process.waitFor()
            project.logger.lifecycle("BuildTools exited with code ${process.exitValue()}.")

            if (process.exitValue() == 0) {
                copyServerJar()
            } else project.logger.error("BuildTools failed to build the server JAR!")
        }
    }
}

val buildPlugin by tasks.registering {
    // First, run build
    // Then copy the resulting jar to the server folder
    // If the server folder does not exist, exit with an error
    description = "Builds the plugin."
    group = "deftu"

    dependsOn("build")
    doLast {
        val serverFolder = project.projectDir.resolve("server")
        if (!serverFolder.exists()) {
            project.logger.error("The server folder does not exist! Run the setupSpigot task first.")
            return@doLast
        }

        val buildTask = project.tasks.named<Jar>("jar").get()
        val pluginJar = buildTask.archiveFile.get().asFile
        val serverPluginFolder = serverFolder.resolve("plugins")

        project.logger.lifecycle("Copying plugin JAR...")
        serverPluginFolder.mkdirs()
        pluginJar.copyTo(serverPluginFolder.resolve(pluginJar.name), overwrite = true)
    }
}

val startSpigot by tasks.registering {
    description = "Starts the Spigot test server."
    group = "deftu"

    doLast {
        val serverFolder = project.projectDir.resolve("server")
        if (!serverFolder.exists()) {
            project.logger.error("The server folder does not exist! Run the setupSpigot task first.")
            return@doLast
        }

        val serverJar = serverFolder.resolve("spigot-${mcData.versionStr}.jar")
        if (!serverJar.exists()) {
            project.logger.error("The server JAR does not exist! Run the setupSpigot task first.")
            return@doLast
        }

        val process = ProcessBuilder("java", "-jar", serverJar.absolutePath, "--nogui").directory(serverFolder).start()
        val inputThread = Thread({
            println("Spigot input thread started.")
            while (true) {
                val line = readLine() ?: break
                println("[Spigot] $line")
                process.outputStream.write(line.toByteArray())
            }
        }, "Spigot Input Thread")
        inputThread.start()
        process.inputStream.bufferedReader().forEachLine { project.logger.lifecycle("[Spigot] $it") }
        process.errorStream.bufferedReader().forEachLine { project.logger.error("[Spigot] $it") }
        process.waitFor()
        inputThread.interrupt()
        project.logger.lifecycle("Spigot exited with code ${process.exitValue()}.")
    }
}

val hasSpigotSetup: Boolean
    get() = project.buildDir.resolve("spigot-${mcData.versionStr}.jar").exists()

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation("org.spigotmc:spigot-api:${mcData.versionStr}+")
    if (hasSpigotSetup) {
        implementation(files(project.buildDir.resolve("spigot-${mcData.versionStr}.jar")))
    }
}
