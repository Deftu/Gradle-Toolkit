package dev.deftu.gradle.utils.version

sealed interface VersionParseResult {

    fun <T : MinecraftVersion> get(): T? {
        return (this as? VersionParseSuccess)?.version as? T
    }

    fun <T : MinecraftVersion> getOrThrow(): T {
        return when (this) {
            is VersionParseSuccess -> version as T
            is VersionParseError -> throw MinecraftVersionParsingException(message)
        }
    }

}

open class VersionParseSuccess(open val version: MinecraftVersion) : VersionParseResult

open class VersionParseError(open val message: String) : VersionParseResult
