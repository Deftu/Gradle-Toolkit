package dev.deftu.gradle.utils.version

sealed interface VersionParseResult {

    fun <T : MinecraftVersion<T>> get(): T? {
        return (this as? VersionParseSuccess)?.version as? T
    }

    fun <T : MinecraftVersion<T>> getOrThrow(): T {
        return when (this) {
            is VersionParseSuccess -> version as T
            is VersionParseError -> throw MinecraftVersionParsingException(message)
        }
    }

}

open class VersionParseSuccess(open val version: MinecraftVersion<*>) : VersionParseResult

open class VersionParseError(open val message: String) : VersionParseResult
