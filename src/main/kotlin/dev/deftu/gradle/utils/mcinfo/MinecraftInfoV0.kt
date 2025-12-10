package dev.deftu.gradle.utils.mcinfo

import dev.deftu.gradle.utils.version.MinecraftVersions

class MinecraftInfoV0 : MinecraftInfo() {
    override var fabricLoaderVersion = "0.16.14"
    override var fabricLanguageKotlinVersion = "1.12.1+kotlin.2.0.20"

    override fun initialize() {
        fabricYarnVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "1.21.11+build.2",
            MinecraftVersions.VERSION_1_21_10 to "1.21.10+build.1",
            MinecraftVersions.VERSION_1_21_9 to "1.21.9+build.1",
            MinecraftVersions.VERSION_1_21_8 to "1.21.8+build.1",
            MinecraftVersions.VERSION_1_21_7 to "1.21.7+build.2",
            MinecraftVersions.VERSION_1_21_6 to "1.21.6+build.1",
            MinecraftVersions.VERSION_1_21_5 to "1.21.5+build.1",
            MinecraftVersions.VERSION_1_21_4 to "1.21.4+build.8",
            MinecraftVersions.VERSION_1_21_3 to "1.21.3+build.2",
            MinecraftVersions.VERSION_1_21_2 to "1.21.2+build.1",
            MinecraftVersions.VERSION_1_21_1 to "1.21.1+build.3",
            MinecraftVersions.VERSION_1_21 to "1.21+build.9",
            MinecraftVersions.VERSION_1_20_6 to "1.20.6+build.3",
            MinecraftVersions.VERSION_1_20_5 to "1.20.5+build.1",
            MinecraftVersions.VERSION_1_20_4 to "1.20.4+build.3",
            MinecraftVersions.VERSION_1_20_3 to "1.20.3+build.1",
            MinecraftVersions.VERSION_1_20_2 to "1.20.2+build.4",
            MinecraftVersions.VERSION_1_20_1 to "1.20.1+build.10",
            MinecraftVersions.VERSION_1_20 to "1.20+build.1",

            MinecraftVersions.VERSION_1_19_4 to "1.19.4+build.2",
            MinecraftVersions.VERSION_1_19_3 to "1.19.3+build.5",
            MinecraftVersions.VERSION_1_19_2 to "1.19.2+build.8",
            MinecraftVersions.VERSION_1_19_1 to "1.19.1+build.6",
            MinecraftVersions.VERSION_1_19 to "1.19+build.4",

            MinecraftVersions.VERSION_1_18_2 to "1.18.2+build.4",
            MinecraftVersions.VERSION_1_18_1 to "1.18.1+build.22",
            MinecraftVersions.VERSION_1_18 to "1.18+build.1",

            MinecraftVersions.VERSION_1_17_1 to "1.17.1+build.65",
            MinecraftVersions.VERSION_1_17 to "1.17+build.13",

            MinecraftVersions.VERSION_1_16_5 to "1.16.5+build.10",
            MinecraftVersions.VERSION_1_16_4 to "1.16.4+build.9",
            MinecraftVersions.VERSION_1_16_3 to "1.16.3+build.47",
            MinecraftVersions.VERSION_1_16_2 to "1.16.2+build.47",
            MinecraftVersions.VERSION_1_16_1 to "1.16.1+build.21",
            MinecraftVersions.VERSION_1_16 to "1.16+build.4",

            MinecraftVersions.VERSION_1_15_2 to "1.15.2+build.17",
            MinecraftVersions.VERSION_1_15_1 to "1.15.1+build.37",
            MinecraftVersions.VERSION_1_15 to "1.15+build.2",

            MinecraftVersions.VERSION_1_14_4 to "1.14.4+build.18",
            MinecraftVersions.VERSION_1_14_3 to "1.14.3+build.13",
            MinecraftVersions.VERSION_1_14_2 to "1.14.2+build.7",
            MinecraftVersions.VERSION_1_14_1 to "1.14.1+build.10",
            MinecraftVersions.VERSION_1_14 to "1.14+build.21"
        ))

        fabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "0.139.4+1.21.11",
            MinecraftVersions.VERSION_1_21_10 to "0.134.1+1.21.10",
            MinecraftVersions.VERSION_1_21_9 to "0.133.14+1.21.9",
            MinecraftVersions.VERSION_1_21_9 to "0.133.14+1.21.9",
            MinecraftVersions.VERSION_1_21_8 to "0.129.0+1.21.8",
            MinecraftVersions.VERSION_1_21_7 to "0.128.2+1.21.7",
            MinecraftVersions.VERSION_1_21_6 to "0.127.1+1.21.6",
            MinecraftVersions.VERSION_1_21_5 to "0.127.1+1.21.5",
            MinecraftVersions.VERSION_1_21_4 to "0.119.3+1.21.4",
            MinecraftVersions.VERSION_1_21_3 to "0.114.1+1.21.3",
            MinecraftVersions.VERSION_1_21_2 to "0.106.1+1.21.2",
            MinecraftVersions.VERSION_1_21_1 to "0.116.3+1.21.1",
            MinecraftVersions.VERSION_1_21 to "0.102.0+1.21",
            MinecraftVersions.VERSION_1_20_6 to "0.100.8+1.20.6",
            MinecraftVersions.VERSION_1_20_5 to "0.97.8+1.20.5",
            MinecraftVersions.VERSION_1_20_4 to "0.97.3+1.20.4",
            MinecraftVersions.VERSION_1_20_3 to "0.91.1+1.20.3",
            MinecraftVersions.VERSION_1_20_2 to "0.91.6+1.20.2",
            MinecraftVersions.VERSION_1_20_1 to "0.92.6+1.20.1",
            MinecraftVersions.VERSION_1_20 to "0.83.0+1.20",

            MinecraftVersions.VERSION_1_19_4 to "0.87.2+1.19.4",
            MinecraftVersions.VERSION_1_19_3 to "0.76.1+1.19.3",
            MinecraftVersions.VERSION_1_19_2 to "0.77.0+1.19.2",
            MinecraftVersions.VERSION_1_19_1 to "0.58.5+1.19.1",
            MinecraftVersions.VERSION_1_19 to "0.58.0+1.19",

            MinecraftVersions.VERSION_1_18_2 to "0.77.0+1.18.2",
            MinecraftVersions.VERSION_1_18_1 to "0.46.6+1.18",
            MinecraftVersions.VERSION_1_18 to "0.46.6+1.18",

            MinecraftVersions.VERSION_1_17_1 to "0.46.1+1.17",
            MinecraftVersions.VERSION_1_17 to "0.46.1+1.17",

            MinecraftVersions.VERSION_1_16_5 to "0.42.0+1.16",
            MinecraftVersions.VERSION_1_16_4 to "0.42.0+1.16",
            MinecraftVersions.VERSION_1_16_3 to "0.42.0+1.16",
            MinecraftVersions.VERSION_1_16_2 to "0.42.0+1.16",
            MinecraftVersions.VERSION_1_16_1 to "0.42.0+1.16",
            MinecraftVersions.VERSION_1_16 to "0.42.0+1.16",

            MinecraftVersions.VERSION_1_15_2 to "0.28.5+1.15",
            MinecraftVersions.VERSION_1_15_1 to "0.28.5+1.15",
            MinecraftVersions.VERSION_1_15 to "0.28.5+1.15",

            MinecraftVersions.VERSION_1_14_4 to "0.28.5+1.14",
            MinecraftVersions.VERSION_1_14_3 to "0.28.5+1.14",
            MinecraftVersions.VERSION_1_14_2 to "0.28.5+1.14",
            MinecraftVersions.VERSION_1_14_1 to "0.28.5+1.14"
        ))

        fabricModMenuDefinitions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to ("com.terraformersmc:modmenu:" to "17.0.0-alpha.1"),
            MinecraftVersions.VERSION_1_21_10 to ("com.terraformersmc:modmenu:" to "16.0.0-rc.1"),
            MinecraftVersions.VERSION_1_21_9 to ("com.terraformersmc:modmenu:" to "16.0.0-rc.1"),
            MinecraftVersions.VERSION_1_21_8 to ("com.terraformersmc:modmenu:" to "15.0.0-beta.3"),
            MinecraftVersions.VERSION_1_21_7 to ("com.terraformersmc:modmenu:" to "15.0.0-beta.3"),
            MinecraftVersions.VERSION_1_21_6 to ("com.terraformersmc:modmenu:" to "15.0.0-beta.3"),
            MinecraftVersions.VERSION_1_21_5 to ("com.terraformersmc:modmenu:" to "14.0.0-rc.2"),
            MinecraftVersions.VERSION_1_21_4 to ("com.terraformersmc:modmenu:" to "13.0.3"),
            MinecraftVersions.VERSION_1_21_3 to ("com.terraformersmc:modmenu:" to "12.0.0"),
            MinecraftVersions.VERSION_1_21_2 to ("com.terraformersmc:modmenu:" to "12.0.0"),
            MinecraftVersions.VERSION_1_21_1 to ("com.terraformersmc:modmenu:" to "11.0.3"),
            MinecraftVersions.VERSION_1_21 to ("com.terraformersmc:modmenu:" to "11.0.3"),

            MinecraftVersions.VERSION_1_20_6 to ("com.terraformersmc:modmenu:" to "10.0.0-beta.1"),
            MinecraftVersions.VERSION_1_20_5 to ("com.terraformersmc:modmenu:" to "10.0.0-beta.1"),
            MinecraftVersions.VERSION_1_20_4 to ("com.terraformersmc:modmenu:" to "9.0.0-pre.1"),
            MinecraftVersions.VERSION_1_20_3 to ("com.terraformersmc:modmenu:" to "9.0.0-pre.1"),
            MinecraftVersions.VERSION_1_20_2 to ("com.terraformersmc:modmenu:" to "8.0.0"),
            MinecraftVersions.VERSION_1_20_1 to ("com.terraformersmc:modmenu:" to "7.2.2"),
            MinecraftVersions.VERSION_1_20 to ("com.terraformersmc:modmenu:" to "7.0.1"),

            MinecraftVersions.VERSION_1_19_4 to ("com.terraformersmc:modmenu:" to "6.2.2"),
            MinecraftVersions.VERSION_1_19_3 to ("com.terraformersmc:modmenu:" to "5.0.2"),
            MinecraftVersions.VERSION_1_19_2 to ("com.terraformersmc:modmenu:" to "4.1.2"),

            MinecraftVersions.VERSION_1_18_2 to ("com.terraformersmc:modmenu:" to "3.2.5"),

            MinecraftVersions.VERSION_1_17_1 to ("com.terraformersmc:modmenu:" to "2.0.17"),

            MinecraftVersions.VERSION_1_16_5 to ("com.terraformersmc:modmenu:" to "1.16.23"),

            MinecraftVersions.VERSION_1_14_4 to ("io.github.prospector:modmenu:" to "1.7.17+build.1")
        ))

        legacyFabricYarnVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_13_2 to "1.13.2+build.563:v2",
            MinecraftVersions.VERSION_1_12_2 to "1.12.2+build.563:v2",
            MinecraftVersions.VERSION_1_11_2 to "1.11.2+build.563:v2",
            MinecraftVersions.VERSION_1_10_2 to "1.10.2+build.562:v2",
            MinecraftVersions.VERSION_1_9_4 to "1.9.4+build.563:v2",
            MinecraftVersions.VERSION_1_8_9 to "1.8.9+build.563:v2"
        ))

        legacyFabricApiVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_12_2 to "1.11.1+1.12.2",
            MinecraftVersions.VERSION_1_11_2 to "1.11.1+1.11.2",
            MinecraftVersions.VERSION_1_10_2 to "1.11.1+1.10.2",
            MinecraftVersions.VERSION_1_9_4 to "1.11.1+1.9.4",
            MinecraftVersions.VERSION_1_8_9 to "1.11.1+1.8.9"
        ))

        kotlinForForgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_9 to "6.0.0",
            MinecraftVersions.VERSION_1_20_6 to "5.9.0",
            MinecraftVersions.VERSION_1_20_5 to "5.0.2",
            MinecraftVersions.VERSION_1_19_3 to "4.11.0",
            MinecraftVersions.VERSION_1_18 to "3.12.0",
            MinecraftVersions.VERSION_1_17 to "2.2.0",
            MinecraftVersions.VERSION_1_14_4 to "1.17.0",
        ))

        forgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "1.21.11-61.0.1",
            MinecraftVersions.VERSION_1_21_10 to "1.21.10-60.1.5",
            MinecraftVersions.VERSION_1_21_9 to "1.21.9-59.0.5",
            MinecraftVersions.VERSION_1_21_8 to "1.21.8-58.0.0",
            MinecraftVersions.VERSION_1_21_7 to "1.21.7-57.0.2",
            MinecraftVersions.VERSION_1_21_6 to "1.21.6-56.0.7",
            MinecraftVersions.VERSION_1_21_5 to "1.21.5-55.0.23",
            MinecraftVersions.VERSION_1_21_4 to "1.21.4-54.0.16",
            MinecraftVersions.VERSION_1_21_3 to "1.21.3-53.0.37",
            MinecraftVersions.VERSION_1_21_1 to "1.21.1-52.0.40",
            MinecraftVersions.VERSION_1_21 to "1.21-51.0.33",

            MinecraftVersions.VERSION_1_20_6 to "1.20.6-50.1.31",
            MinecraftVersions.VERSION_1_20_4 to "1.20.4-49.1.0",
            MinecraftVersions.VERSION_1_20_3 to "1.20.3-49.0.2",
            MinecraftVersions.VERSION_1_20_2 to "1.20.2-48.0.18",
            MinecraftVersions.VERSION_1_20_1 to "1.20.1-47.2.1",
            MinecraftVersions.VERSION_1_20 to "1.20-46.0.14",

            MinecraftVersions.VERSION_1_19_4 to "1.19.4-45.0.66",
            MinecraftVersions.VERSION_1_19_3 to "1.19.3-44.1.23",
            MinecraftVersions.VERSION_1_19_2 to "1.19.2-43.2.4",
            MinecraftVersions.VERSION_1_19_1 to "1.19.1-42.0.9",
            MinecraftVersions.VERSION_1_19 to "1.19-41.1.0",

            MinecraftVersions.VERSION_1_18_2 to "1.18.2-40.1.73",
            MinecraftVersions.VERSION_1_18_1 to "1.18.1-39.1.2",
            MinecraftVersions.VERSION_1_18 to "1.18-38.0.17",

            MinecraftVersions.VERSION_1_17_1 to "1.17.1-37.1.1",

            MinecraftVersions.VERSION_1_16_5 to "1.16.5-36.2.39",
            MinecraftVersions.VERSION_1_16_4 to "1.16.4-35.1.37",
            MinecraftVersions.VERSION_1_16_3 to "1.16.3-34.1.42",
            MinecraftVersions.VERSION_1_16_2 to "1.16.2-33.0.61",
            MinecraftVersions.VERSION_1_16_1 to "1.16.1-32.0.108",

            MinecraftVersions.VERSION_1_15_2 to "1.15.2-31.2.57",
            MinecraftVersions.VERSION_1_15_1 to "1.15.1-30.0.51",
            MinecraftVersions.VERSION_1_15 to "1.15-29.0.4",

            MinecraftVersions.VERSION_1_14_4 to "1.14.4-28.2.26",
            MinecraftVersions.VERSION_1_14_3 to "1.14.3-27.0.60",
            MinecraftVersions.VERSION_1_14_2 to "1.14.2-26.0.63",

            MinecraftVersions.VERSION_1_13_2 to "1.13.2-25.0.223",

            MinecraftVersions.VERSION_1_12_2 to "1.12.2-14.23.5.2847",
            MinecraftVersions.VERSION_1_12_1 to "1.12.1-14.22.1.2485",
            MinecraftVersions.VERSION_1_12 to "1.12-14.21.1.2443",

            MinecraftVersions.VERSION_1_11_2 to "1.11.2-13.20.1.2588",
            MinecraftVersions.VERSION_1_11 to "1.11-13.19.1.2199",

            MinecraftVersions.VERSION_1_10_2 to "1.10.2-12.18.3.2511",
            MinecraftVersions.VERSION_1_10 to "1.10-12.18.0.2000-1.10.0",

            MinecraftVersions.VERSION_1_9_4 to "1.9.4-12.17.0.2317-1.9.4",
            MinecraftVersions.VERSION_1_9 to "1.9-12.16.1.1938-1.9.0",

            MinecraftVersions.VERSION_1_8_9 to "1.8.9-11.15.1.2318-1.8.9"
        ))

        mcpDefinitions.putAll(listOf(
            MinecraftVersions.VERSION_1_15_2 to "snapshot:20200220-1.15.1@zip",
            MinecraftVersions.VERSION_1_15 to "snapshot:20200220-1.15.1@zip",

            MinecraftVersions.VERSION_1_14_4 to "snapshot:20200119-1.14.4@zip",
            MinecraftVersions.VERSION_1_14_3 to "snapshot:20200119-1.14.3@zip",
            MinecraftVersions.VERSION_1_14_2 to "snapshot:20190624-1.14.2",

            MinecraftVersions.VERSION_1_13_2 to "stable:47-1.13.2",

            MinecraftVersions.VERSION_1_12_2 to "stable:39-1.12",
            MinecraftVersions.VERSION_1_12_1 to "stable:39-1.12",
            MinecraftVersions.VERSION_1_12 to "stable:39-1.12",

            MinecraftVersions.VERSION_1_11_2 to "stable:32-1.11",
            MinecraftVersions.VERSION_1_11 to "stable:32-1.11",

            MinecraftVersions.VERSION_1_10_2 to "stable:29-1.10.2",
            MinecraftVersions.VERSION_1_10 to "stable:29-1.10.2",

            MinecraftVersions.VERSION_1_9_4 to "stable:26-1.9.4",
            MinecraftVersions.VERSION_1_9 to "stable:24-1.9",

            MinecraftVersions.VERSION_1_8_9 to "stable:22-1.8.9"
        ))

        neoForgeVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_11 to "21.11.0-beta",
            MinecraftVersions.VERSION_1_21_10 to "21.10.0-beta",
            MinecraftVersions.VERSION_1_21_9 to "21.9.0-beta",
            MinecraftVersions.VERSION_1_21_8 to "21.8.2-beta",
            MinecraftVersions.VERSION_1_21_7 to "21.7.1-beta",
            MinecraftVersions.VERSION_1_21_6 to "21.6.11-beta",
            MinecraftVersions.VERSION_1_21_5 to "21.5.39-beta",
            MinecraftVersions.VERSION_1_21_4 to "21.4.123",
            MinecraftVersions.VERSION_1_21_3 to "21.3.63",
            MinecraftVersions.VERSION_1_21_2 to "21.2.1-beta",
            MinecraftVersions.VERSION_1_21_1 to "21.1.117",
            MinecraftVersions.VERSION_1_21 to "21.0.167",

            MinecraftVersions.VERSION_1_20_6 to "20.6.121",
            MinecraftVersions.VERSION_1_20_5 to "20.5.21-beta",
            MinecraftVersions.VERSION_1_20_4 to "20.4.237",
            MinecraftVersions.VERSION_1_20_3 to "20.3.8-beta",
            MinecraftVersions.VERSION_1_20_2 to "20.2.88"
        ))

        parchmentVersions.putAll(listOf(
            MinecraftVersions.VERSION_1_21_10 to "1.21.10:2025.10.12",
            MinecraftVersions.VERSION_1_21_9 to "1.21.9:2025.10.05",
            MinecraftVersions.VERSION_1_21_8 to "1.21.8:2025.09.14",
            MinecraftVersions.VERSION_1_21_7 to "1.21.7:2025.07.18",
            MinecraftVersions.VERSION_1_21_6 to "1.21.6:2025.06.29",
            MinecraftVersions.VERSION_1_21_5 to "1.21.5:2025.06.15",
            MinecraftVersions.VERSION_1_21_4 to "1.21.4:2025.03.23",
            MinecraftVersions.VERSION_1_21_3 to "1.21.3:2024.12.07",
            MinecraftVersions.VERSION_1_21_2 to "1.21.1:2024.11.17",
            MinecraftVersions.VERSION_1_21_1 to "1.21.1:2024.11.17",
            MinecraftVersions.VERSION_1_21 to "1.21:2024.07.07",
            MinecraftVersions.VERSION_1_20_6 to "1.20.6:2024.06.16",
            MinecraftVersions.VERSION_1_20_4 to "1.20.4:2024.04.14",
            MinecraftVersions.VERSION_1_20_3 to "1.20.3:2023.12.31",
            MinecraftVersions.VERSION_1_20_2 to "1.20.2:2023.12.10",
            MinecraftVersions.VERSION_1_20_1 to "1.20.1:2023.09.03",
            MinecraftVersions.VERSION_1_19_4 to "1.19.4:2023.06.26",
            MinecraftVersions.VERSION_1_19_3 to "1.19.3:2023.06.25",
            MinecraftVersions.VERSION_1_19_2 to "1.19.2:2022.11.27",
            MinecraftVersions.VERSION_1_18_2 to "1.18.2:2022.11.06",
            MinecraftVersions.VERSION_1_17_1 to "1.17.1:2021.12.12",
            MinecraftVersions.VERSION_1_16_5 to "1.16.5:2022.03.06"
        ))
    }
}
