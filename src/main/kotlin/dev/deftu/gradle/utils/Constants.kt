package dev.deftu.gradle.utils

import java.io.File

object Constants {
    val debug: Boolean
        get() = System.getProperty("dgt.debug", "false").toBoolean()
    val dir: File
        get() = File(System.getenv("DGT_DIR") ?: File(System.getProperty("user.home"), ".dgt").absolutePath)
}
