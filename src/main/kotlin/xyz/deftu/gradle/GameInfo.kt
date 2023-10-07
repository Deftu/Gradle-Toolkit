package xyz.deftu.gradle

object GameInfo {
    const val FABRIC_LOADER_VERSION = "0.14.22"
    const val FABRIC_LANGUAGE_KOTLIN_VERSION = "1.7.4+kotlin.1.6.21"

    private val fabricApiVersions: Map<Int, String> = mapOf(
        1_20_02 to "0.89.3+1.20.2",
        1_20_01 to "0.89.3+1.20.1",
        1_20_00 to "0.83.0+1.20",

        1_19_04 to "0.82.0+1.19.4",
        1_19_03 to "0.76.1+1.19.3",
        1_19_02 to "0.76.0+1.19.2",
        1_19_01 to "0.58.5+1.19.1",
        1_19_00 to "0.58.0+1.19",

        1_18_02 to "0.76.0+1.18.2",
        1_18_01 to "0.46.6+1.18",
        1_18_00 to "0.46.6+1.18",

        1_17_01 to "0.46.1+1.17",
        1_17_00 to "0.46.1+1.17",

        1_16_05 to "0.42.0+1.16",
        1_16_04 to "0.42.0+1.16",
        1_16_03 to "0.42.0+1.16",
        1_16_02 to "0.42.0+1.16",
        1_16_01 to "0.42.0+1.16",
        1_16_00 to "0.42.0+1.16",

        1_15_02 to "0.28.5+1.15",
        1_15_01 to "0.28.5+1.15",
        1_15_00 to "0.28.5+1.15",

        1_14_04 to "0.28.5+1.14",
        1_14_03 to "0.28.5+1.14",
        1_14_02 to "0.28.5+1.14",
        1_14_01 to "0.28.5+1.14",
        1_14_00 to "0.28.5+1.14",
    )

    private val modMenuVersions: Map<Int, Pair<String, String>> = mapOf(
        1_20_02 to ("com.terraformersmc:modmenu:" to "8.0.0"),
        1_20_01 to ("com.terraformersmc:modmenu:" to "7.2.2"),
        1_20_00 to ("com.terraformersmc:modmenu:" to "7.0.1"),
        1_19_04 to ("com.terraformersmc:modmenu:" to "6.2.2"),
        1_19_03 to ("com.terraformersmc:modmenu:" to "5.0.2"),
        1_19_02 to ("com.terraformersmc:modmenu:" to "4.1.2"),
        1_18_02 to ("com.terraformersmc:modmenu:" to "3.2.5"),
        1_17_01 to ("com.terraformersmc:modmenu:" to "2.0.17"),
        1_16_05 to ("com.terraformersmc:modmenu:" to "1.16.23"),
        1_14_04 to ("io.github.prospector:modmenu:" to "1.7.17+build.1"),
    )

    private val yarnVersions: Map<Int, String> = mapOf(
        1_20_02 to "1.20.2+build.4",
        1_20_01 to "1.20.1+build.10",
        1_20_00 to "1.20+build.1",

        1_19_04 to "1.19.4+build.2",
        1_19_03 to "1.19.3+build.5",
        1_19_02 to "1.19.2+build.8",
        1_19_01 to "1.19.1+build.6",
        1_19_00 to "1.19+build.4",

        1_18_02 to "1.18.2+build.4",
        1_18_01 to "1.18.1+build.22",
        1_18_00 to "1.18+build.1",

        1_17_01 to "1.17.1+build.65",
        1_17_00 to "1.17+build.13",

        1_16_05 to "1.16.5+build.10",
        1_16_04 to "1.16.4+build.9",
        1_16_03 to "1.16.3+build.47",
        1_16_02 to "1.16.2+build.47",
        1_16_01 to "1.16.1+build.21",
        1_16_00 to "1.16+build.4",

        1_15_02 to "1.15.2+build.17",
        1_15_01 to "1.15.1+build.37",
        1_15_00 to "1.15+build.2",

        1_14_04 to "1.14.4+build.18",
        1_14_03 to "1.14.3+build.13",
        1_14_02 to "1.14.2+build.7",
        1_14_01 to "1.14.1+build.10",
        1_14_00 to "1.14+build.21"
    )

    private val forgeVersions: Map<Int, String> = mapOf(
        1_20_02 to "1.20.2-48.0.18",
        1_20_01 to "1.20.1-47.2.1",
        1_20_00 to "1.20-46.0.14",

        1_19_04 to "1.19.4-45.0.66",
        1_19_03 to "1.19.3-44.1.23",
        1_19_02 to "1.19.2-43.2.4",
        1_19_01 to "1.19.1-42.0.9",
        1_19_00 to "1.19-41.1.0",

        1_18_02 to "1.18.2-40.1.73",
        1_18_01 to "1.18.1-39.1.2",
        1_18_00 to "1.18-38.0.17",

        1_17_01 to "1.17.1-37.1.1",

        1_16_05 to "1.16.5-36.2.39",
        1_16_04 to "1.16.4-35.1.37",
        1_16_03 to "1.16.3-34.1.42",
        1_16_02 to "1.16.2-33.0.61",
        1_16_01 to "1.16.1-32.0.108",

        1_15_02 to "1.15.2-31.2.57",
        1_15_01 to "1.15.1-30.0.51",
        1_15_00 to "1.15-29.0.4",

        1_14_04 to "1.14.4-28.2.26",
        1_14_03 to "1.14.3-27.0.60",
        1_14_02 to "1.14.2-26.0.63",

        1_13_02 to "1.13.2-25.0.223",

        1_12_02 to "1.12.2-14.23.5.2847",
        1_12_01 to "1.12.1-14.22.1.2485",
        1_12_00 to "1.12-14.21.1.2443",

        1_11_02 to "1.11.2-13.20.1.2588",
        1_11_00 to "1.11-13.19.1.2199",

        1_10_02 to "1.10.2-12.18.3.2511",
        1_10_00 to "1.10-12.18.0.2000-1.10.0",

        1_09_04 to "1.9.4-12.17.0.2317-1.9.4",
        1_09_00 to "1.9-12.16.1.1938-1.9.0",

        1_08_09 to "1.8.9-11.15.1.2318-1.8.9",
        1_08_08 to "1.8.8-11.15.0.1655",
        1_08_00 to "1.8-11.14.4.1577",

        1_07_10 to "1.7.10-10.13.4.1614-1.7.10",
        1_07_02 to "1.7.2-10.12.2.1161-mc172"
    )

    private val mcpMappings: Map<Int, String> = mapOf(
        1_15_02 to "snapshot:20200220-1.15.1@zip",
        1_15_00 to "snapshot:20200220-1.15.1@zip",

        1_14_04 to "snapshot:20200119-1.14.4@zip",
        1_14_03 to "snapshot:20200119-1.14.3@zip",
        1_14_02 to "snapshot:20190624-1.14.2",

        1_13_02 to "stable:47-1.13.2",

        1_12_02 to "stable:39-1.12",
        1_12_01 to "stable:39-1.12",
        1_12_00 to "stable:39-1.12",

        1_11_02 to "stable:32-1.11",
        1_11_00 to "stable:32-1.11",

        1_10_02 to "stable:29-1.10.2",
        1_10_00 to "stable:29-1.10.2",

        1_09_04 to "stable:26-1.9.4",
        1_09_00 to "stable:24-1.9",

        1_08_09 to "stable:22-1.8.9",
        1_08_08 to "stable:20-1.8.8",
        1_08_00 to "stable:18-1.8",

        1_07_10 to "stable:12-1.7.10",
        1_07_02 to "stable:12-1.7.10"
    )

    fun fetchLatestFabricApiVersion() =
        fabricApiVersions[0]
    fun fetchFabricApiVersion(mcVersion: Int) =
        fabricApiVersions[mcVersion]

    fun fetchLatestModMenuVersion() =
        modMenuVersions[0]
    fun fetchModMenuVersion(mcVersion: Int) =
        modMenuVersions[mcVersion]

    fun fetchLatestForgeVersion() =
        forgeVersions[0]
    fun fetchForgeVersion(mcVersion: Int) =
        forgeVersions[mcVersion]

    fun fetchLatestYarnMappings() =
        yarnVersions[0]
    fun fetchYarnMappings(mcVersion: Int) =
        yarnVersions[mcVersion]

    fun fetchLatestMcpMappings() =
        mcpMappings[0]
    fun fetchMcpMappings(mcVersion: Int) =
        mcpMappings[mcVersion]

    fun fetchKotlinForForgeVersion(mcVersion: Int): String {
        return if (mcVersion >= 1_19_02) {
            "4.3.0"
        } else if (mcVersion >= 1_17_00) {
            "3.12.0"
        } else if (mcVersion >= 1_14_04) {
            "2.0.1"
        } else "1.16.0"
    }
}
