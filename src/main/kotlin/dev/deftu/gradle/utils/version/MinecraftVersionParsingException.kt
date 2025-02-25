package dev.deftu.gradle.utils.version

class MinecraftVersionParsingException(message: String) : Exception(message) {
    constructor(message: String, cause: Throwable) : this(message) {
        initCause(cause)
    }
}
