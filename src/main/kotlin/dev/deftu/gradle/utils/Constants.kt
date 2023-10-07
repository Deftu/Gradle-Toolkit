package dev.deftu.gradle.utils

object Constants {
    val debug: Boolean
        get() = System.getProperty("dgt.debug", "false").toBoolean()
}
