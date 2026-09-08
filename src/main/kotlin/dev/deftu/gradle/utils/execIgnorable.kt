package dev.deftu.gradle.utils

import org.gradle.api.Project
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.kotlin.dsl.of
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import javax.inject.Inject

fun Project.execIgnorable(vararg command: String): String {
    return providers.of(IgnorableCommandValueSource::class) {
        parameters.commands = command.toList()
    }.get()
}

internal abstract class IgnorableCommandValueSource : ValueSource<String, IgnorableCommandValueSource.Parameters> {
    interface Parameters : ValueSourceParameters {
        var commands: List<String>
    }

    @get:Inject
    abstract val execOperations: ExecOperations

    override fun obtain(): String? {
        try {
            val output = ByteArrayOutputStream()
            execOperations.exec {
                commandLine(parameters.commands)
                isIgnoreExitValue = true
                standardOutput = output
                errorOutput = ByteArrayOutputStream() // Suppress error output
            }

            return output.toString().trim()
        } catch (ignored: Exception) {
            // Ignored... Fallback to empty string
            return ""
        }
    }
}
