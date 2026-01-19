package dev.deftu.gradle.utils

import org.gradle.api.Project
import org.gradle.kotlin.dsl.support.serviceOf
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream

fun Project.execIgnorable(vararg command: String): String {
    val output = ByteArrayOutputStream()

    try {
        serviceOf<ExecOperations>().exec {
            commandLine(*command)
            isIgnoreExitValue = true
            standardOutput = output
            errorOutput = ByteArrayOutputStream() // Suppress error output
        }
    } catch (ignored: Exception) {
        // Ignored...
    }

    return output.toString().trim()
}
