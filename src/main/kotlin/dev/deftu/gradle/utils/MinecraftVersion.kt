package dev.deftu.gradle.utils

import org.gradle.api.JavaVersion
import java.io.Serializable

/**
 * A class representing a version of Minecraft.
 */
class MinecraftVersion private constructor(val rawVersion: Int) : Comparable<MinecraftVersion>, Serializable {

    val major: Int
        get() = rawVersion / 10000
    val minor: Int
        get() = rawVersion / 100 % 100
    val patch: Int
        get() = rawVersion % 100

    val patchless: String
        get() = "$major.$minor"

    val javaVersion: JavaVersion
        get() = when {
            this >= VERSION_1_20_5 -> JavaVersion.VERSION_21
            minor >= 18 -> JavaVersion.VERSION_17
            minor >= 17 -> JavaVersion.VERSION_16
            else -> JavaVersion.VERSION_1_8
        }

    val javaVersionString: String
        get() = when (javaVersion) {
            JavaVersion.VERSION_1_8 -> "JAVA_8"
            JavaVersion.VERSION_16 -> "JAVA_16"
            JavaVersion.VERSION_17 -> "JAVA_17"
            JavaVersion.VERSION_21 -> "JAVA_21"
            else -> "UNKNOWN"
        }

    override operator fun compareTo(other: MinecraftVersion): Int {
        return rawVersion.compareTo(other.rawVersion)
    }

    operator fun compareTo(other: Int): Int {
        return rawVersion.compareTo(other)
    }

    override fun equals(other: Any?): Boolean {
        if (other is MinecraftVersion) {
            return rawVersion == other.rawVersion
        }

        if (other is Int) {
            return rawVersion == other
        }

        return false
    }

    override fun hashCode(): Int {
        var result = major.hashCode()
        result = 31 * result + minor.hashCode()
        result = 31 * result + patch.hashCode()
        return result
    }

    override fun toString(): String {
        return "$major.$minor" + if (patch != 0) ".$patch" else ""
    }

    companion object {

        @JvmStatic
        fun from(major: Int, minor: Int, patch: Int): MinecraftVersion {
            return MinecraftVersion(major * 10000 + minor * 100 + patch)
        }

        @JvmStatic
        val UNKNOWN = MinecraftVersion(0)

        @JvmStatic
        val VERSION_1_21_4 = MinecraftVersion(1_21_04)

        @JvmStatic
        val VERSION_1_21_3 = MinecraftVersion(1_21_03)

        @JvmStatic
        val VERSION_1_21_2 = MinecraftVersion(1_21_02)

        @JvmStatic
        val VERSION_1_21_1 = MinecraftVersion(1_21_01)

        @JvmStatic
        val VERSION_1_21 = MinecraftVersion(1_21_00)

        @JvmStatic
        val VERSION_1_20_6 = MinecraftVersion(1_20_06)

        @JvmStatic
        val VERSION_1_20_5 = MinecraftVersion(1_20_05)

        @JvmStatic
        val VERSION_1_20_4 = MinecraftVersion(1_20_04)

        @JvmStatic
        val VERSION_1_20_3 = MinecraftVersion(1_20_03)

        @JvmStatic
        val VERSION_1_20_2 = MinecraftVersion(1_20_02)

        @JvmStatic
        val VERSION_1_20_1 = MinecraftVersion(1_20_01)

        @JvmStatic
        val VERSION_1_20 = MinecraftVersion(1_20_00)

        @JvmStatic
        val VERSION_1_19_4 = MinecraftVersion(1_19_04)

        @JvmStatic
        val VERSION_1_19_3 = MinecraftVersion(1_19_03)

        @JvmStatic
        val VERSION_1_19_2 = MinecraftVersion(1_19_02)

        @JvmStatic
        val VERSION_1_19_1 = MinecraftVersion(1_19_01)

        @JvmStatic
        val VERSION_1_19 = MinecraftVersion(1_19_00)

        @JvmStatic
        val VERSION_1_18_2 = MinecraftVersion(1_18_02)

        @JvmStatic
        val VERSION_1_18_1 = MinecraftVersion(1_18_01)

        @JvmStatic
        val VERSION_1_18 = MinecraftVersion(1_18_00)

        @JvmStatic
        val VERSION_1_17_1 = MinecraftVersion(1_17_01)

        @JvmStatic
        val VERSION_1_17 = MinecraftVersion(1_17_00)

        @JvmStatic
        val VERSION_1_16_5 = MinecraftVersion(1_16_05)

        @JvmStatic
        val VERSION_1_16_4 = MinecraftVersion(1_16_04)

        @JvmStatic
        val VERSION_1_16_3 = MinecraftVersion(1_16_03)

        @JvmStatic
        val VERSION_1_16_2 = MinecraftVersion(1_16_02)

        @JvmStatic
        val VERSION_1_16_1 = MinecraftVersion(1_16_01)

        @JvmStatic
        val VERSION_1_16 = MinecraftVersion(1_16_00)

        @JvmStatic
        val VERSION_1_15_2 = MinecraftVersion(1_15_02)

        @JvmStatic
        val VERSION_1_15_1 = MinecraftVersion(1_15_01)

        @JvmStatic
        val VERSION_1_15 = MinecraftVersion(1_15_00)

        @JvmStatic
        val VERSION_1_14_4 = MinecraftVersion(1_14_04)

        @JvmStatic
        val VERSION_1_14_3 = MinecraftVersion(1_14_03)

        @JvmStatic
        val VERSION_1_14_2 = MinecraftVersion(1_14_02)

        @JvmStatic
        val VERSION_1_14_1 = MinecraftVersion(1_14_01)

        @JvmStatic
        val VERSION_1_14 = MinecraftVersion(1_14_00)

        @JvmStatic
        val VERSION_1_13_2 = MinecraftVersion(1_13_02)

        @JvmStatic
        val VERSION_1_13_1 = MinecraftVersion(1_13_01)

        @JvmStatic
        val VERSION_1_13 = MinecraftVersion(1_13_00)

        @JvmStatic
        val VERSION_1_12_2 = MinecraftVersion(1_12_02)

        @JvmStatic
        val VERSION_1_12_1 = MinecraftVersion(1_12_01)

        @JvmStatic
        val VERSION_1_12 = MinecraftVersion(1_12_00)

        @JvmStatic
        val VERSION_1_11_2 = MinecraftVersion(1_11_02)

        @JvmStatic
        val VERSION_1_11_1 = MinecraftVersion(1_11_01)

        @JvmStatic
        val VERSION_1_11 = MinecraftVersion(1_11_00)

        @JvmStatic
        val VERSION_1_10_2 = MinecraftVersion(1_10_02)

        @JvmStatic
        val VERSION_1_10_1 = MinecraftVersion(1_10_01)

        @JvmStatic
        val VERSION_1_10 = MinecraftVersion(1_10_00)

        @JvmStatic
        val VERSION_1_9_4 = MinecraftVersion(1_09_04)

        @JvmStatic
        val VERSION_1_9_3 = MinecraftVersion(1_09_03)

        @JvmStatic
        val VERSION_1_9_2 = MinecraftVersion(1_09_02)

        @JvmStatic
        val VERSION_1_9_1 = MinecraftVersion(1_09_01)

        @JvmStatic
        val VERSION_1_9 = MinecraftVersion(1_09_00)

        @JvmStatic
        val VERSION_1_8_9 = MinecraftVersion(1_08_09)

        @JvmStatic
        val all = listOf(
            VERSION_1_21, VERSION_1_20_6, VERSION_1_20_5,
            VERSION_1_20_4, VERSION_1_20_3, VERSION_1_20_2,
            VERSION_1_20_1, VERSION_1_20, VERSION_1_19_4,
            VERSION_1_19_3, VERSION_1_19_2, VERSION_1_19_1,
            VERSION_1_19, VERSION_1_18_2, VERSION_1_18_1,
            VERSION_1_18, VERSION_1_17_1, VERSION_1_17,
            VERSION_1_16_5, VERSION_1_16_4, VERSION_1_16_3,
            VERSION_1_16_2, VERSION_1_16_1, VERSION_1_16,
            VERSION_1_15_2, VERSION_1_15_1, VERSION_1_15,
            VERSION_1_14_4, VERSION_1_14_3, VERSION_1_14_2,
            VERSION_1_14_1, VERSION_1_14, VERSION_1_13_2,
            VERSION_1_13_1, VERSION_1_13, VERSION_1_12_2,
            VERSION_1_12_1, VERSION_1_12, VERSION_1_11_2,
            VERSION_1_11_1, VERSION_1_11, VERSION_1_10_2,
            VERSION_1_10_1, VERSION_1_10, VERSION_1_9_4,
            VERSION_1_9_3, VERSION_1_9_2, VERSION_1_9_1,
            VERSION_1_9, VERSION_1_8_9
        )

    }

}
