package dev.deftu.gradle.utils.version

import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Calendar

class MinecraftDropVersion private constructor(
    val rawVersion: Int,
    val rawClassifier: String? = null,
    val build: Int? = null,
) : MinecraftVersion<MinecraftDropVersion> {
    companion object {
        @JvmStatic
        internal val regex = "(?<year>\\d+)\\.(?<drop>\\d+)(?:\\.(?<hotfix>\\d+))?(?:-(?<classifier>snapshot|pre|rc)-(?<build>\\d+))?".toRegex()

        @JvmStatic
        internal val parser: MinecraftVersionParser = "drop" to { _, version: String ->
            if (version.startsWith("1.")) {
                return@to VersionParseError("Legacy '1.x' versions are not supported by MinecraftDropVersion parser: $version")
            }

            try {
                val match = regex.find(version) ?: throw IllegalArgumentException("Invalid version format, could not match to regex: $version")
                val groups = match.groups

                val year = groups["year"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing major version: $version")
                val drop = groups["drop"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing minor version: $version")
                val hotfix = groups["hotfix"]?.value?.toInt() ?: 0
                val classifier = groups["classifier"]?.value
                val build = groups["build"]?.value?.toInt()

                val raw = year * 10000 + drop * 100 + hotfix
                VersionParseSuccess(MinecraftDropVersion(raw, classifier, build))
            } catch (t: Throwable) {
                val message = t.message
                if (message == null) {
                    t.printStackTrace()
                }

                VersionParseError(message ?: "Unknown error")
            }
        }

        @JvmStatic
        fun from(version: Int): MinecraftDropVersion {
            return MinecraftDropVersion(version)
        }

        @JvmStatic
        fun from(year: Int, drop: Int, hotfix: Int): MinecraftDropVersion {
            return from(year * 10000 + drop * 100 + hotfix)
        }

        @JvmStatic
        fun from(version: String): MinecraftDropVersion {
            return parser.second(null, version).getOrThrow()
        }
    }

    enum class Classifier(val identifier: String) {
        SNAPSHOT("snapshot"), // Oldest
        PRE_RELEASE("pre"), // Newer than snapshots
        RELEASE_CANDIDATE("rc"); // Newer than pre-release

        companion object {
            @JvmStatic
            fun from(identifier: String): Classifier? {
                return values().firstOrNull { it.identifier.equals(identifier, ignoreCase = true) }
            }
        }
    }

    val year: Int
        get() = rawVersion / 10000

    val drop: Int
        get() = rawVersion / 100 % 100

    val patch: Int
        get() = rawVersion % 100

    val classifier: Classifier? by lazy {
        rawClassifier?.let(Classifier::from)
    }

    override val releaseTime: OffsetDateTime by lazy {
        MinecraftVersions.getReleaseTimeForVersionOptionally(toString())
            // If we can't infer it from Mojang's data, we'll need to derive it from the drop version content
            ?: run {
                println("Could not find release time for Minecraft drop version $this from Mojang data, deriving from drop version...")
                val normalizedYear = year + 2000

                val date = if (drop <= 5) {
                    val month = ((drop - 1) * (12 / 5)) + 1
                    LocalDate.of(normalizedYear, month.coerceIn(1, 12), 1)
                } else {
                    LocalDate.of(normalizedYear, 12, 1)
                }

                OffsetDateTime.of(date, LocalTime.MIDNIGHT, ZoneOffset.UTC)
            }
    }

    override val preprocessorKey: Int = rawVersion

    override val isRelease: Boolean
        get() = classifier == null

    override val isSnapshot: Boolean
        get() = classifier == Classifier.SNAPSHOT

    /**
     * We need to override this because Fabric explicitly overrides "snapshot" to "alpha" for ease of sorting.
     */
    override val fabricId: String
        get() {
            return buildString {
                append("${year}.${drop}")
                if (patch != 0) {
                    append(".${patch}")
                }

                if (rawClassifier != null && build != null) {
                    val fabricClassifier = if (classifier == Classifier.SNAPSHOT) "alpha" else rawClassifier
                    append("-${fabricClassifier}.$build")
                }
            }
        }

    override fun compareTo(other: MinecraftVersion<*>): Int {
        if (other !is MinecraftDropVersion) {
            return releaseTime.compareTo(other.releaseTime)
        }

        if (rawVersion != other.rawVersion) {
            return rawVersion.compareTo(other.rawVersion)
        }

        if (classifier == null && other.classifier != null) {
            return 1
        }

        if (classifier != null && other.classifier == null) {
            return -1
        }

        if (classifier != null && other.classifier != null) {
            if (classifier != other.classifier) {
                return classifier!!.compareTo(other.classifier!!)
            }

            return (build ?: 0).compareTo(other.build ?: 0)
        }

        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (other is MinecraftDropVersion) {
            return rawVersion == other.rawVersion &&
                rawClassifier == other.rawClassifier &&
                build == other.build
        }

        return false
    }

    override fun hashCode(): Int {
        var result = rawVersion
        result = 31 * result + (rawClassifier?.hashCode() ?: 0)
        result = 31 * result + (build ?: 0)
        return result
    }

    override fun toString(): String {
        return buildString {
            append("${year}.${drop}")
            if (patch != 0) {
                append(".${patch}")
            }

            if (rawClassifier != null && build != null) {
                append("-${rawClassifier}-$build")
            }
        }
    }
}
