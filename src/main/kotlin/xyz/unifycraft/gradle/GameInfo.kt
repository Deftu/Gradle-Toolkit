package xyz.unifycraft.gradle

object GameInfo {
    val infoMap: Map<String, Map<Int, String>> = mapOf(
        "fabric_loader_version" to mapOf(
            0 to "0.13.3"
        ), "forge_version" to mapOf(
            11802 to "1.18.2-40.1.51",
            11801 to "1.18.1-39.0.79",
            11701 to "1.17.1-37.0.112",
            11602 to "1.16.2-33.0.61",
            11502 to "1.15.2-31.1.18",
            11404 to "1.14.4-28.1.113",
            11202 to "1.12.2-14.23.0.2486",
            10809 to "1.8.9-11.15.1.2318-1.8.9",
            10800 to "1.8-11.14.4.1563",
            10710 to "1.7.10-10.13.4.1558-1.7.10"
        ), "yarn_mappings" to mapOf(
            11802 to "1.18.2+build.3",
            11801 to "1.18.2+build.2:v2",
            11701 to "1.17.1+build.39:v2",
            11602 to "1.16.4+build.6:v2",
            11502 to "1.15.2+build.14",
            11404 to "1.14.4+build.16"
        ), "mcp_mappings" to mapOf(
            11602 to "snapshot:20201028-1.16.3",
            11502 to "snapshot:20200220-1.15.1@zip",
            11404 to "snapshot:20190719-1.14.3",
            11202 to "snapshot:20170615-1.12",
            10809 to "stable:22-1.8.9",
            10800 to "snapshot:20141130-1.8",
            10710 to "stable:12-1.7.10"
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
