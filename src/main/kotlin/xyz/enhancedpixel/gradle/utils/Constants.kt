package xyz.enhancedpixel.gradle.utils

object Constants {
    val debug: Boolean
        get() = System.getProperty("epgt.debug", "false").toBoolean()
}
