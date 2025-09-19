package dev.deftu.gradle.utils.version

import dev.deftu.gradle.utils.propertyOr
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.Serializable
import java.time.OffsetDateTime

sealed interface MinecraftVersion<T : MinecraftVersion<T>> : Comparable<MinecraftVersion<*>>, Serializable {
    companion object {
        @JvmStatic
        fun getRawVersion(project: Project): String {
            return project.propertyOr("minecraft.version", project.name, prefix = "")
        }
    }

    object Unknown : MinecraftVersion<Unknown> {
        override val releaseTime: OffsetDateTime = OffsetDateTime.MIN

        override val preprocessorKey: Int = 0

        override fun compareTo(other: MinecraftVersion<*>): Int {
            return 0
        }
    }

    val releaseTime: OffsetDateTime

    val preprocessorKey: Int

    val javaVersion: JavaVersion
        get() = when {
            this >= MinecraftVersions.VERSION_1_20_5 -> JavaVersion.VERSION_21
            this >= MinecraftVersions.VERSION_1_18 -> JavaVersion.VERSION_17
            this >= MinecraftVersions.VERSION_1_17 -> JavaVersion.VERSION_16
            else -> JavaVersion.VERSION_1_8
        }

    fun isNewerThan(other: T): Boolean {
        return compareTo(other) > 0
    }

    fun isOlderThan(other: T): Boolean {
        return compareTo(other) < 0
    }
}
