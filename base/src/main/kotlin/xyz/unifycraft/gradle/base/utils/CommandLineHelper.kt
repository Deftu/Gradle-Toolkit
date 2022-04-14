package xyz.unifycraft.gradle.base.utils

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object CommandLineHelper {
    fun fetchCommandOutput(project: Project, vararg commands: String): String {
        val output = ByteArrayOutputStream()
        project.exec {
            commandLine(*commands)
            standardOutput = output
        }
        return output.toString().trim { it <= ' ' }
    }
}