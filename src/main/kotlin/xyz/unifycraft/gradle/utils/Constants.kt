package xyz.unifycraft.gradle.utils

object Constants {
    val debug: Boolean
        get() = System.getProperty("ucgt.debug", "false").toBoolean()
}
