package xyz.unifycraft.gradle.loomconfig

class MinecraftVersionException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}