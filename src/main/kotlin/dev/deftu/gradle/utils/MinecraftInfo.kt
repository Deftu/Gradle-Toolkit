package dev.deftu.gradle.utils

object MinecraftInfo {

    object Fabric {

        const val LOADER_VERSION = "0.16.9"
        const val KOTLIN_DEP_VERSION = "1.12.1+kotlin.2.0.20"

        private val yarnVersions = MinecraftVersionMap(
            MinecraftVersion.VERSION_1_21_4 to "1.21.4+build.7",
            MinecraftVersion.VERSION_1_21_3 to "1.21.3+build.2",
            MinecraftVersion.VERSION_1_21_2 to "1.21.2+build.1",
            MinecraftVersion.VERSION_1_21_1 to "1.21.1+build.3",
            MinecraftVersion.VERSION_1_21 to "1.21+build.9",
            MinecraftVersion.VERSION_1_20_6 to "1.20.6+build.3",
            MinecraftVersion.VERSION_1_20_5 to "1.20.5+build.1",
            MinecraftVersion.VERSION_1_20_4 to "1.20.4+build.3",
            MinecraftVersion.VERSION_1_20_3 to "1.20.3+build.1",
            MinecraftVersion.VERSION_1_20_2 to "1.20.2+build.4",
            MinecraftVersion.VERSION_1_20_1 to "1.20.1+build.10",
            MinecraftVersion.VERSION_1_20 to "1.20+build.1",

            MinecraftVersion.VERSION_1_19_4 to "1.19.4+build.2",
            MinecraftVersion.VERSION_1_19_3 to "1.19.3+build.5",
            MinecraftVersion.VERSION_1_19_2 to "1.19.2+build.8",
            MinecraftVersion.VERSION_1_19_1 to "1.19.1+build.6",
            MinecraftVersion.VERSION_1_19 to "1.19+build.4",

            MinecraftVersion.VERSION_1_18_2 to "1.18.2+build.4",
            MinecraftVersion.VERSION_1_18_1 to "1.18.1+build.22",
            MinecraftVersion.VERSION_1_18 to "1.18+build.1",

            MinecraftVersion.VERSION_1_17_1 to "1.17.1+build.65",
            MinecraftVersion.VERSION_1_17 to "1.17+build.13",

            MinecraftVersion.VERSION_1_16_5 to "1.16.5+build.10",
            MinecraftVersion.VERSION_1_16_4 to "1.16.4+build.9",
            MinecraftVersion.VERSION_1_16_3 to "1.16.3+build.47",
            MinecraftVersion.VERSION_1_16_2 to "1.16.2+build.47",
            MinecraftVersion.VERSION_1_16_1 to "1.16.1+build.21",
            MinecraftVersion.VERSION_1_16 to "1.16+build.4",

            MinecraftVersion.VERSION_1_15_2 to "1.15.2+build.17",
            MinecraftVersion.VERSION_1_15_1 to "1.15.1+build.37",
            MinecraftVersion.VERSION_1_15 to "1.15+build.2",

            MinecraftVersion.VERSION_1_14_4 to "1.14.4+build.18",
            MinecraftVersion.VERSION_1_14_3 to "1.14.3+build.13",
            MinecraftVersion.VERSION_1_14_2 to "1.14.2+build.7",
            MinecraftVersion.VERSION_1_14_1 to "1.14.1+build.10",
            MinecraftVersion.VERSION_1_14 to "1.14+build.21"
        )

        private val fabricApiVersions = MinecraftVersionMap(
            MinecraftVersion.VERSION_1_21_4 to "0.114.0+1.21.4",
            MinecraftVersion.VERSION_1_21_3 to "0.114.0+1.21.3",
            MinecraftVersion.VERSION_1_21_2 to "0.106.1+1.21.2",
            MinecraftVersion.VERSION_1_21_1 to "0.114.0+1.21.1",
            MinecraftVersion.VERSION_1_21 to "0.102.0+1.21",
            MinecraftVersion.VERSION_1_20_6 to "0.100.8+1.20.6",
            MinecraftVersion.VERSION_1_20_5 to "0.97.8+1.20.5",
            MinecraftVersion.VERSION_1_20_4 to "0.97.2+1.20.4",
            MinecraftVersion.VERSION_1_20_3 to "0.91.1+1.20.3",
            MinecraftVersion.VERSION_1_20_2 to "0.91.6+1.20.2",
            MinecraftVersion.VERSION_1_20_1 to "0.92.3+1.20.1",
            MinecraftVersion.VERSION_1_20 to "0.83.0+1.20",

            MinecraftVersion.VERSION_1_19_4 to "0.87.2+1.19.4",
            MinecraftVersion.VERSION_1_19_3 to "0.76.1+1.19.3",
            MinecraftVersion.VERSION_1_19_2 to "0.77.0+1.19.2",
            MinecraftVersion.VERSION_1_19_1 to "0.58.5+1.19.1",
            MinecraftVersion.VERSION_1_19 to "0.58.0+1.19",

            MinecraftVersion.VERSION_1_18_2 to "0.77.0+1.18.2",
            MinecraftVersion.VERSION_1_18_1 to "0.46.6+1.18",
            MinecraftVersion.VERSION_1_18 to "0.46.6+1.18",

            MinecraftVersion.VERSION_1_17_1 to "0.46.1+1.17",
            MinecraftVersion.VERSION_1_17 to "0.46.1+1.17",

            MinecraftVersion.VERSION_1_16_5 to "0.42.0+1.16",
            MinecraftVersion.VERSION_1_16_4 to "0.42.0+1.16",
            MinecraftVersion.VERSION_1_16_3 to "0.42.0+1.16",
            MinecraftVersion.VERSION_1_16_2 to "0.42.0+1.16",
            MinecraftVersion.VERSION_1_16_1 to "0.42.0+1.16",
            MinecraftVersion.VERSION_1_16 to "0.42.0+1.16",

            MinecraftVersion.VERSION_1_15_2 to "0.28.5+1.15",
            MinecraftVersion.VERSION_1_15_1 to "0.28.5+1.15",
            MinecraftVersion.VERSION_1_15 to "0.28.5+1.15",

            MinecraftVersion.VERSION_1_14_4 to "0.28.5+1.14",
            MinecraftVersion.VERSION_1_14_3 to "0.28.5+1.14",
            MinecraftVersion.VERSION_1_14_2 to "0.28.5+1.14",
            MinecraftVersion.VERSION_1_14_1 to "0.28.5+1.14"
        )

        private val modMenuDependencies = MinecraftVersionMap(
            MinecraftVersion.VERSION_1_21_4 to ("com.terraformersmc:modmenu:" to "13.0.0-beta.1"),
            MinecraftVersion.VERSION_1_21_3 to ("com.terraformersmc:modmenu:" to "12.0.0"),
            MinecraftVersion.VERSION_1_21_2 to ("com.terraformersmc:modmenu:" to "12.0.0"),
            MinecraftVersion.VERSION_1_21_1 to ("com.terraformersmc:modmenu:" to "11.0.3"),
            MinecraftVersion.VERSION_1_21 to ("com.terraformersmc:modmenu:" to "11.0.3"),

            MinecraftVersion.VERSION_1_20_6 to ("com.terraformersmc:modmenu:" to "10.0.0-beta.1"),
            MinecraftVersion.VERSION_1_20_5 to ("com.terraformersmc:modmenu:" to "10.0.0-beta.1"),
            MinecraftVersion.VERSION_1_20_4 to ("com.terraformersmc:modmenu:" to "9.0.0-pre.1"),
            MinecraftVersion.VERSION_1_20_3 to ("com.terraformersmc:modmenu:" to "9.0.0-pre.1"),
            MinecraftVersion.VERSION_1_20_2 to ("com.terraformersmc:modmenu:" to "8.0.0"),
            MinecraftVersion.VERSION_1_20_1 to ("com.terraformersmc:modmenu:" to "7.2.2"),
            MinecraftVersion.VERSION_1_20 to ("com.terraformersmc:modmenu:" to "7.0.1"),

            MinecraftVersion.VERSION_1_19_4 to ("com.terraformersmc:modmenu:" to "6.2.2"),
            MinecraftVersion.VERSION_1_19_3 to ("com.terraformersmc:modmenu:" to "5.0.2"),
            MinecraftVersion.VERSION_1_19_2 to ("com.terraformersmc:modmenu:" to "4.1.2"),

            MinecraftVersion.VERSION_1_18_2 to ("com.terraformersmc:modmenu:" to "3.2.5"),

            MinecraftVersion.VERSION_1_17_1 to ("com.terraformersmc:modmenu:" to "2.0.17"),

            MinecraftVersion.VERSION_1_16_5 to ("com.terraformersmc:modmenu:" to "1.16.23"),

            MinecraftVersion.VERSION_1_14_4 to ("io.github.prospector:modmenu:" to "1.7.17+build.1")
        )

        @JvmStatic
        fun getYarnVersion(version: MinecraftVersion): String {
            return yarnVersions[version] ?: throw IllegalArgumentException("Unknown version for Yarn mappings: $version")
        }

        @JvmStatic
        fun getFabricApiVersion(version: MinecraftVersion): String {
            return fabricApiVersions[version] ?: throw IllegalArgumentException("Unknown version for Fabric API: $version")
        }

        @JvmStatic
        fun getModMenuDependency(version: MinecraftVersion): Pair<String, String> {
            return modMenuDependencies[version] ?: throw IllegalArgumentException("Unknown version for Mod Menu: $version")
        }

    }

    object LegacyFabric {

        private val legacyYarnVersions = MinecraftVersionMap(
            MinecraftVersion.VERSION_1_13_2 to "1.13.2+build.541:v2",
            MinecraftVersion.VERSION_1_12_2 to "1.12.2+build.541:v2",
            MinecraftVersion.VERSION_1_11_2 to "1.11.2+build.541:v2",
            MinecraftVersion.VERSION_1_10_2 to "1.10.2+build.541:v2",
            MinecraftVersion.VERSION_1_9_4 to "1.9.4+build.541:v2",
            MinecraftVersion.VERSION_1_8_9 to "1.8.9+build.541:v2"
        )

        private val legacyFabricApiVersions = MinecraftVersionMap(
            MinecraftVersion.VERSION_1_13_2 to "1.9.4+1.13.2",
            MinecraftVersion.VERSION_1_12_2 to "1.9.4+1.12.2",
            MinecraftVersion.VERSION_1_11_2 to "1.9.4+1.11.2",
            MinecraftVersion.VERSION_1_10_2 to "1.9.4+1.10.2",
            MinecraftVersion.VERSION_1_9_4 to "1.9.4+1.9.4",
            MinecraftVersion.VERSION_1_8_9 to "1.9.4+1.8.9"
        )

        @JvmStatic
        fun getLegacyYarnVersion(version: MinecraftVersion): String {
            return legacyYarnVersions[version] ?: throw IllegalArgumentException("Unknown version for (legacy) Yarn mappings: $version")
        }

        @JvmStatic
        fun getLegacyFabricApiVersion(version: MinecraftVersion): String {
            return legacyFabricApiVersions[version] ?: throw IllegalArgumentException("Unknown version for (legacy) Fabric API: $version")
        }

    }

    object ForgeLike {

        @JvmStatic
        fun getKotlinForForgeVersion(version: MinecraftVersion): String {
            return when {
                version >= MinecraftVersion.VERSION_1_20_6 -> "5.7.0"
                version >= MinecraftVersion.VERSION_1_20_5 -> "5.0.2"
                version >= MinecraftVersion.VERSION_1_19_3 -> "4.11.0"
                version >= MinecraftVersion.VERSION_1_18 -> "3.12.0"
                version >= MinecraftVersion.VERSION_1_17 -> "2.2.0"
                version >= MinecraftVersion.VERSION_1_14_4 -> "1.17.0"
                else -> throw IllegalArgumentException("Unknown version for Kotlin for Forge: $version")
            }
        }

    }

    object Forge {

        private val forgeVersions = MinecraftVersionMap(
            MinecraftVersion.VERSION_1_21_4 to "1.21.4-54.0.16",
            MinecraftVersion.VERSION_1_21_3 to "1.21.3-53.0.37",
            MinecraftVersion.VERSION_1_21_1 to "1.21.1-52.0.40",
            MinecraftVersion.VERSION_1_21 to "1.21-51.0.33",

            MinecraftVersion.VERSION_1_20_6 to "1.20.6-50.1.31",
            MinecraftVersion.VERSION_1_20_4 to "1.20.4-49.1.0",
            MinecraftVersion.VERSION_1_20_3 to "1.20.3-49.0.2",
            MinecraftVersion.VERSION_1_20_2 to "1.20.2-48.0.18",
            MinecraftVersion.VERSION_1_20_1 to "1.20.1-47.2.1",
            MinecraftVersion.VERSION_1_20 to "1.20-46.0.14",

            MinecraftVersion.VERSION_1_19_4 to "1.19.4-45.0.66",
            MinecraftVersion.VERSION_1_19_3 to "1.19.3-44.1.23",
            MinecraftVersion.VERSION_1_19_2 to "1.19.2-43.2.4",
            MinecraftVersion.VERSION_1_19_1 to "1.19.1-42.0.9",
            MinecraftVersion.VERSION_1_19 to "1.19-41.1.0",

            MinecraftVersion.VERSION_1_18_2 to "1.18.2-40.1.73",
            MinecraftVersion.VERSION_1_18_1 to "1.18.1-39.1.2",
            MinecraftVersion.VERSION_1_18 to "1.18-38.0.17",

            MinecraftVersion.VERSION_1_17_1 to "1.17.1-37.1.1",

            MinecraftVersion.VERSION_1_16_5 to "1.16.5-36.2.39",
            MinecraftVersion.VERSION_1_16_4 to "1.16.4-35.1.37",
            MinecraftVersion.VERSION_1_16_3 to "1.16.3-34.1.42",
            MinecraftVersion.VERSION_1_16_2 to "1.16.2-33.0.61",
            MinecraftVersion.VERSION_1_16_1 to "1.16.1-32.0.108",

            MinecraftVersion.VERSION_1_15_2 to "1.15.2-31.2.57",
            MinecraftVersion.VERSION_1_15_1 to "1.15.1-30.0.51",
            MinecraftVersion.VERSION_1_15 to "1.15-29.0.4",

            MinecraftVersion.VERSION_1_14_4 to "1.14.4-28.2.26",
            MinecraftVersion.VERSION_1_14_3 to "1.14.3-27.0.60",
            MinecraftVersion.VERSION_1_14_2 to "1.14.2-26.0.63",

            MinecraftVersion.VERSION_1_13_2 to "1.13.2-25.0.223",

            MinecraftVersion.VERSION_1_12_2 to "1.12.2-14.23.5.2847",
            MinecraftVersion.VERSION_1_12_1 to "1.12.1-14.22.1.2485",
            MinecraftVersion.VERSION_1_12 to "1.12-14.21.1.2443",

            MinecraftVersion.VERSION_1_11_2 to "1.11.2-13.20.1.2588",
            MinecraftVersion.VERSION_1_11 to "1.11-13.19.1.2199",

            MinecraftVersion.VERSION_1_10_2 to "1.10.2-12.18.3.2511",
            MinecraftVersion.VERSION_1_10 to "1.10-12.18.0.2000-1.10.0",

            MinecraftVersion.VERSION_1_9_4 to "1.9.4-12.17.0.2317-1.9.4",
            MinecraftVersion.VERSION_1_9 to "1.9-12.16.1.1938-1.9.0",

            MinecraftVersion.VERSION_1_8_9 to "1.8.9-11.15.1.2318-1.8.9"
        )

        private val mcpDependencies = MinecraftVersionMap(
            MinecraftVersion.VERSION_1_15_2 to "snapshot:20200220-1.15.1@zip",
            MinecraftVersion.VERSION_1_15 to "snapshot:20200220-1.15.1@zip",

            MinecraftVersion.VERSION_1_14_4 to "snapshot:20200119-1.14.4@zip",
            MinecraftVersion.VERSION_1_14_3 to "snapshot:20200119-1.14.3@zip",
            MinecraftVersion.VERSION_1_14_2 to "snapshot:20190624-1.14.2",

            MinecraftVersion.VERSION_1_13_2 to "stable:47-1.13.2",

            MinecraftVersion.VERSION_1_12_2 to "stable:39-1.12",
            MinecraftVersion.VERSION_1_12_1 to "stable:39-1.12",
            MinecraftVersion.VERSION_1_12 to "stable:39-1.12",

            MinecraftVersion.VERSION_1_11_2 to "stable:32-1.11",
            MinecraftVersion.VERSION_1_11 to "stable:32-1.11",

            MinecraftVersion.VERSION_1_10_2 to "stable:29-1.10.2",
            MinecraftVersion.VERSION_1_10 to "stable:29-1.10.2",

            MinecraftVersion.VERSION_1_9_4 to "stable:26-1.9.4",
            MinecraftVersion.VERSION_1_9 to "stable:24-1.9",

            MinecraftVersion.VERSION_1_8_9 to "stable:22-1.8.9"
        )

        @JvmStatic
        fun getForgeVersion(version: MinecraftVersion): String {
            return forgeVersions[version] ?: throw IllegalArgumentException("Unknown version for Forge: $version")
        }

        @JvmStatic
        fun getMcpDependency(version: MinecraftVersion): String {
            val mcpVersion = mcpDependencies[version] ?: throw IllegalArgumentException("Unknown version for MCP: $version")

            return "de.oceanlabs.mcp:mcp_$mcpVersion"
        }

    }

    object NeoForge {

        private val neoForgeVersions = MinecraftVersionMap(
            MinecraftVersion.VERSION_1_21_4 to "21.4.49",
            MinecraftVersion.VERSION_1_21_3 to "21.3.58",
            MinecraftVersion.VERSION_1_21_2 to "21.2.2.1-beta",
            MinecraftVersion.VERSION_1_21_1 to "21.1.93",
            MinecraftVersion.VERSION_1_21 to "21.0.167",

            MinecraftVersion.VERSION_1_20_6 to "20.6.121",
            MinecraftVersion.VERSION_1_20_5 to "20.5.21-beta",
            MinecraftVersion.VERSION_1_20_4 to "20.4.237",
            MinecraftVersion.VERSION_1_20_3 to "20.3.8-beta",
            MinecraftVersion.VERSION_1_20_2 to "20.2.88"
        )

        @JvmStatic
        fun getNeoForgeVersion(version: MinecraftVersion): String {
            return neoForgeVersions[version] ?: throw IllegalArgumentException("Unknown version for NeoForged: $version")
        }

    }

    private val parchmentVersions = MinecraftVersionMap(
        MinecraftVersion.VERSION_1_21_4 to "1.21.4:2024.12.29",
        MinecraftVersion.VERSION_1_21_3 to "1.21.3:2024.12.07",
        MinecraftVersion.VERSION_1_21_2 to "1.21.1:2024.11.17",
        MinecraftVersion.VERSION_1_21_1 to "1.21.1:2024.11.17",
        MinecraftVersion.VERSION_1_21 to "1.21:2024.07.07",
        MinecraftVersion.VERSION_1_20_6 to "1.20.6:2024.06.16",
        MinecraftVersion.VERSION_1_20_4 to "1.20.4:2024.04.14",
        MinecraftVersion.VERSION_1_20_3 to "1.20.3:2023.12.31",
        MinecraftVersion.VERSION_1_20_2 to "1.20.2:2023.12.10",
        MinecraftVersion.VERSION_1_20_1 to "1.20.1:2023.09.03",
        MinecraftVersion.VERSION_1_19_4 to "1.19.4:2023.06.26",
        MinecraftVersion.VERSION_1_19_3 to "1.19.3:2023.06.25",
        MinecraftVersion.VERSION_1_19_2 to "1.19.2:2022.11.27",
        MinecraftVersion.VERSION_1_18_2 to "1.18.2:2022.11.06",
        MinecraftVersion.VERSION_1_17_1 to "1.17.1:2021.12.12",
        MinecraftVersion.VERSION_1_16_5 to "1.16.5:2022.03.06"
    )

    @JvmStatic
    fun getParchmentVersion(version: MinecraftVersion): String {
        return parchmentVersions[version] ?: throw IllegalArgumentException("Unknown version for Parchment: $version")
    }

}
