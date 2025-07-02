package dev.deftu.gradle.utils.version

import com.google.gson.JsonParser
import dev.deftu.gradle.ToolkitConstants
import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.Project
import java.io.File
import java.time.OffsetDateTime

internal typealias MinecraftVersionParser = Pair<String, ((Project?, String) -> VersionParseResult)>

object MinecraftVersions {

    private const val MOJANG_VERSION_MANIFEST = "https://launchermeta.mojang.com/mc/game/version_manifest_v2.json"

    private val parsers: Set<MinecraftVersionParser>
        get() = setOf(MinecraftReleaseVersion.parser, MinecraftSnapshotVersion.parser)

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    /**
     * [MOJANG_VERSION_MANIFEST]
     */
    private val cachedVersionReleaseTimes = mutableMapOf<String, OffsetDateTime>()

    /**
     * .dgt/minecraft_versions.json
     */
    private val cacheFile: File by lazy {
        val file = File(ToolkitConstants.dir, "minecraft_versions.json")
        if (!file.parentFile.exists() && !file.parentFile.mkdirs()) {
            throw IllegalStateException("Failed to create cache directory")
        }

        if (!file.exists() && !file.createNewFile()) {
            throw IllegalStateException("Failed to create cache file")
        }

        file
    }

    @JvmStatic
    val UNKNOWN = MinecraftVersion.Unknown

    @JvmStatic
    val VERSION_1_21_7 = MinecraftReleaseVersion.from(1_21_07)

    @JvmStatic
    val VERSION_1_21_6 = MinecraftReleaseVersion.from(1_21_06)

    @JvmStatic
    val VERSION_1_21_5 = MinecraftReleaseVersion.from(1_21_05)

    @JvmStatic
    val VERSION_1_21_4 = MinecraftReleaseVersion.from(1_21_04)

    @JvmStatic
    val VERSION_1_21_3 = MinecraftReleaseVersion.from(1_21_03)

    @JvmStatic
    val VERSION_1_21_2 = MinecraftReleaseVersion.from(1_21_02)

    @JvmStatic
    val VERSION_1_21_1 = MinecraftReleaseVersion.from(1_21_01)

    @JvmStatic
    val VERSION_1_21 = MinecraftReleaseVersion.from(1_21_00)

    @JvmStatic
    val VERSION_1_20_6 = MinecraftReleaseVersion.from(1_20_06)

    @JvmStatic
    val VERSION_1_20_5 = MinecraftReleaseVersion.from(1_20_05)

    @JvmStatic
    val VERSION_1_20_4 = MinecraftReleaseVersion.from(1_20_04)

    @JvmStatic
    val VERSION_1_20_3 = MinecraftReleaseVersion.from(1_20_03)

    @JvmStatic
    val VERSION_1_20_2 = MinecraftReleaseVersion.from(1_20_02)

    @JvmStatic
    val VERSION_1_20_1 = MinecraftReleaseVersion.from(1_20_01)

    @JvmStatic
    val VERSION_1_20 = MinecraftReleaseVersion.from(1_20_00)

    @JvmStatic
    val VERSION_1_19_4 = MinecraftReleaseVersion.from(1_19_04)

    @JvmStatic
    val VERSION_1_19_3 = MinecraftReleaseVersion.from(1_19_03)

    @JvmStatic
    val VERSION_1_19_2 = MinecraftReleaseVersion.from(1_19_02)

    @JvmStatic
    val VERSION_1_19_1 = MinecraftReleaseVersion.from(1_19_01)

    @JvmStatic
    val VERSION_1_19 = MinecraftReleaseVersion.from(1_19_00)

    @JvmStatic
    val VERSION_1_18_2 = MinecraftReleaseVersion.from(1_18_02)

    @JvmStatic
    val VERSION_1_18_1 = MinecraftReleaseVersion.from(1_18_01)

    @JvmStatic
    val VERSION_1_18 = MinecraftReleaseVersion.from(1_18_00)

    @JvmStatic
    val VERSION_1_17_1 = MinecraftReleaseVersion.from(1_17_01)

    @JvmStatic
    val VERSION_1_17 = MinecraftReleaseVersion.from(1_17_00)

    @JvmStatic
    val VERSION_1_16_5 = MinecraftReleaseVersion.from(1_16_05)

    @JvmStatic
    val VERSION_1_16_4 = MinecraftReleaseVersion.from(1_16_04)

    @JvmStatic
    val VERSION_1_16_3 = MinecraftReleaseVersion.from(1_16_03)

    @JvmStatic
    val VERSION_1_16_2 = MinecraftReleaseVersion.from(1_16_02)

    @JvmStatic
    val VERSION_1_16_1 = MinecraftReleaseVersion.from(1_16_01)

    @JvmStatic
    val VERSION_1_16 = MinecraftReleaseVersion.from(1_16_00)

    @JvmStatic
    val VERSION_1_15_2 = MinecraftReleaseVersion.from(1_15_02)

    @JvmStatic
    val VERSION_1_15_1 = MinecraftReleaseVersion.from(1_15_01)

    @JvmStatic
    val VERSION_1_15 = MinecraftReleaseVersion.from(1_15_00)

    @JvmStatic
    val VERSION_1_14_4 = MinecraftReleaseVersion.from(1_14_04)

    @JvmStatic
    val VERSION_1_14_3 = MinecraftReleaseVersion.from(1_14_03)

    @JvmStatic
    val VERSION_1_14_2 = MinecraftReleaseVersion.from(1_14_02)

    @JvmStatic
    val VERSION_1_14_1 = MinecraftReleaseVersion.from(1_14_01)

    @JvmStatic
    val VERSION_1_14 = MinecraftReleaseVersion.from(1_14_00)

    @JvmStatic
    val VERSION_1_13_2 = MinecraftReleaseVersion.from(1_13_02)

    @JvmStatic
    val VERSION_1_13_1 = MinecraftReleaseVersion.from(1_13_01)

    @JvmStatic
    val VERSION_1_13 = MinecraftReleaseVersion.from(1_13_00)

    @JvmStatic
    val VERSION_1_12_2 = MinecraftReleaseVersion.from(1_12_02)

    @JvmStatic
    val VERSION_1_12_1 = MinecraftReleaseVersion.from(1_12_01)

    @JvmStatic
    val VERSION_1_12 = MinecraftReleaseVersion.from(1_12_00)

    @JvmStatic
    val VERSION_1_11_2 = MinecraftReleaseVersion.from(1_11_02)

    @JvmStatic
    val VERSION_1_11_1 = MinecraftReleaseVersion.from(1_11_01)

    @JvmStatic
    val VERSION_1_11 = MinecraftReleaseVersion.from(1_11_00)

    @JvmStatic
    val VERSION_1_10_2 = MinecraftReleaseVersion.from(1_10_02)

    @JvmStatic
    val VERSION_1_10_1 = MinecraftReleaseVersion.from(1_10_01)

    @JvmStatic
    val VERSION_1_10 = MinecraftReleaseVersion.from(1_10_00)

    @JvmStatic
    val VERSION_1_9_4 = MinecraftReleaseVersion.from(1_09_04)

    @JvmStatic
    val VERSION_1_9_3 = MinecraftReleaseVersion.from(1_09_03)

    @JvmStatic
    val VERSION_1_9_2 = MinecraftReleaseVersion.from(1_09_02)

    @JvmStatic
    val VERSION_1_9_1 = MinecraftReleaseVersion.from(1_09_01)

    @JvmStatic
    val VERSION_1_9 = MinecraftReleaseVersion.from(1_09_00)

    @JvmStatic
    val VERSION_1_8_9 = MinecraftReleaseVersion.from(1_08_09)

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

    @JvmStatic
    fun get(project: Project, version: String): MinecraftVersion<*> {
        val collectedErrors = mutableMapOf<String, VersionParseError>()
        for ((type, parser) in parsers) {
            val result = parser(project, version)
            if (result is VersionParseSuccess) {
                return result.version
            } else if (result is VersionParseError) {
                collectedErrors[type] = result
            }
        }

        collectedErrors.forEach { (type, error) ->
            project.logger.warn("- Failed to parse version '$version' as $type: ${error.message}")
        }

        throw MinecraftVersionParsingException("Couldn't parse $version as a valid Minecraft version!\n" + collectedErrors.map { (type, error) ->
            "- [$type] $error"
        }.joinToString("\n"))
    }

    @JvmStatic
    fun getReleaseTimeFor(identifier: String): OffsetDateTime {
        if (cachedVersionReleaseTimes.isEmpty()) {
            populateReleaseTimes() // Block thread until we populate it
        }

        return cachedVersionReleaseTimes[identifier] ?: OffsetDateTime.MIN
    }

    private fun populateReleaseTimes() {
        try {
            val response = httpClient.newCall(
                Request.Builder()
                    .url(MOJANG_VERSION_MANIFEST)
                    .get()
                    .build()
            ).execute()
            val body = response.body?.string()
            if (body == null) {
                response.close()
                populateReleaseTimesLocally()
                return
            }

            response.close()

            populateFrom(body)
            cacheFile.writeText(body)
        } catch (t: Throwable) {
            populateReleaseTimesLocally()
        }
    }

    private fun populateReleaseTimesLocally() {
        if (!cacheFile.exists()) {
            return
        }

        val body = cacheFile.readText()
        populateFrom(body)
    }

    private fun populateFrom(input: String) {
        val manifestJson = JsonParser.parseString(input).asJsonObject
        val versions = manifestJson.getAsJsonArray("versions")
        versions.forEach { version ->
            val versionJson = version.asJsonObject
            val id = versionJson.get("id").asString
            val releaseTime = OffsetDateTime.parse(versionJson.get("releaseTime").asString)
            cachedVersionReleaseTimes[id] = releaseTime
        }
    }

}
