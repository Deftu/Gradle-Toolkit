package xyz.deftu.gradle

object GameInfo {
    val infoMap: Map<String, Map<Int, String>> = mapOf(
        "fabric_loader_version" to mapOf(
            0 to "0.14.12"
        ), "forge_version" to mapOf(
            11902 to "1.19.2-43.1.2",
            11901 to "1.19.1-42.0.9",
            11900 to "1.19-41.1.0",

            11802 to "1.18.2-40.1.73",
            11801 to "1.18.1-39.1.2",
            11800 to "1.18-38.0.17",

            11701 to "1.17.1-37.1.1",

            11605 to "1.16.5-36.2.39",
            11604 to "1.16.4-35.1.37",
            11603 to "1.16.3-34.1.42",
            11602 to "1.16.2-33.0.61",
            11601 to "1.16.1-32.0.108",

            11502 to "1.15.2-31.2.57",
            11501 to "1.15.1-30.0.51",
            11500 to "1.15-29.0.4",

            11404 to "1.14.4-28.2.26",
            11403 to "1.14.3-27.0.60",
            11402 to "1.14.2-26.0.63",

            11302 to "1.13.2-25.0.223",

            11202 to "1.12.2-14.23.5.2847",
            11201 to "1.12.1-14.22.1.2485",
            11200 to "1.12-14.21.1.2443",

            11102 to "1.11.2-13.20.1.2588",
            11100 to "1.11-13.19.1.2199",

            11002 to "1.10.2-12.18.3.2511",
            11000 to "1.10-12.18.0.2000-1.10.0",

            10904 to "1.9.4-12.17.0.2317-1.9.4",
            10900 to "1.9-12.16.1.1938-1.9.0",

            10809 to "1.8.9-11.15.1.2318-1.8.9",
            10808 to "1.8.8-11.15.0.1655",
            10800 to "1.8-11.14.4.1577",

            10710 to "1.7.10-10.13.4.1614-1.7.10",
            10702 to "1.7.2-10.12.2.1161-mc172"
        ), "yarn_mappings" to mapOf(
            11902 to "1.19.2+build.8",
            11901 to "1.19.1+build.6",
            11900 to "1.19+build.4",

            11802 to "1.18.2+build.4",
            11801 to "1.18.1+build.22",
            11800 to "1.18+build.1",

            11701 to "1.17.1+build.65",
            11700 to "1.17+build.13",

            11605 to "1.16.5+build.10",
            11604 to "1.16.4+build.9",
            11603 to "1.16.3+build.47",
            11602 to "1.16.2+build.47",
            11601 to "1.16.1+build.21",
            11600 to "1.16+build.4",

            11502 to "1.15.2+build.17",
            11501 to "1.15.1+build.37",
            11500 to "1.15+build.2",

            11404 to "1.14.4+build.18",
            11403 to "1.14.3+build.13",
            11402 to "1.14.2+build.7",
            11401 to "1.14.1+build.10",
            11400 to "1.14+build.21"
        ), "mcp_mappings" to mapOf(
            11502 to "snapshot:20200220-1.15.1@zip",
            11500 to "snapshot:20200220-1.15.1@zip",

            11404 to "stable:58-1.14.4",
            11403 to "stable:56-1.14.3",
            11402 to "stable:53-1.14.2",
            11401 to "stable:51-1.14.1",
            11400 to "stable:49-1.14",

            11302 to "stable:47-1.13.2",

            11202 to "stable:39-1.12",
            11201 to "stable:39-1.12",
            11200 to "stable:39-1.12",

            11102 to "stable:32-1.11",
            11100 to "stable:32-1.11",

            11002 to "stable:29-1.10.2",
            11000 to "stable:29-1.10.2",

            10904 to "stable:26-1.9.4",
            10900 to "stable:24-1.9",

            10809 to "stable:22-1.8.9",
            10808 to "stable:20-1.8.8",
            10800 to "stable:18-1.8",

            10710 to "stable:12-1.7.10",
            10702 to "stable:12-1.7.10"
        )
    )

    fun fetchFabricLoaderVersion(mcVersion: Int) =
        infoMap["fabric_loader_version"]?.get(mcVersion)
    fun fetchForgeVersion(mcVersion: Int) =
        infoMap["forge_version"]?.get(mcVersion)
    fun fetchYarnMappings(mcVersion: Int) =
        infoMap["yarn_mappings"]?.get(mcVersion)
    fun fetchMcpMappings(mcVersion: Int) =
        infoMap["mcp_mappings"]?.get(mcVersion)
}
