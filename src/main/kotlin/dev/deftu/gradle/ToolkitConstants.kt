package dev.deftu.gradle

import java.io.File

object ToolkitConstants {

    const val TASK_GROUP = "toolkit"

    val debug: Boolean
        get() = System.getProperty("dgt.debug", "false").toBoolean()

    val dir: File
        get() = File(System.getenv("DGT_DIR") ?: File(System.getProperty("user.home"), ".dgt").absolutePath)

}
