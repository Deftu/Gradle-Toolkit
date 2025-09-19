package dev.deftu.gradle.utils.version

import java.time.OffsetDateTime

class MinecraftReleaseVersion private constructor(
    val rawVersion: Int,
    val rawClassifier: String? = null,
    val revision: Int? = null,
) : MinecraftVersion<MinecraftReleaseVersion> {

    companion object {

        @JvmStatic
        internal val regex = "(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?(?:[- ](?<classifier>pre|rc|Pre-Release)[ ]?(?:(?<revision>\\d+))?)?".toRegex()

        @JvmStatic
        internal val parser: MinecraftVersionParser = "release" to { _, version: String ->
            try {
                val match = regex.find(version) ?: throw IllegalArgumentException("Invalid version format, could not match to regex: $version")
                val groups = match.groups

                val major = groups["major"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version forma, missing major version: $version")
                val minor = groups["minor"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing minor version: $version")
                val patch = groups["patch"]?.value?.toInt() ?: 0
                val classifier = groups["classifier"]?.value
                val revision = groups["revision"]?.value?.toInt() ?: 1

                val raw = major * 10000 + minor * 100 + patch
                VersionParseSuccess(MinecraftReleaseVersion(raw, classifier, revision))
            } catch (t: Throwable) {
                val message = t.message
                if (message == null) {
                    t.printStackTrace()
                }

                VersionParseError(message ?: "Unknown error")
            }
        }

        @JvmStatic
        fun from(version: Int): MinecraftReleaseVersion {
            return MinecraftReleaseVersion(version)
        }

        @JvmStatic
        fun from(major: Int, minor: Int, patch: Int): MinecraftReleaseVersion {
            return from(major * 10000 + minor * 100 + patch)
        }

        @JvmStatic
        fun from(version: String): MinecraftReleaseVersion {
            return parser.second(null, version).getOrThrow()
        }

    }

    enum class Classifier(vararg val identifier: String) {
        RELEASE_CANDIDATE("rc"), // Newer than pre-release
        PRE_RELEASE("pre", "Pre-Release"); // Newer than snapshots

        companion object {

            fun from(identifier: String): Classifier? {
                return values().firstOrNull { classifier ->
                    classifier.identifier.contains(identifier)
                }
            }

        }

    }

    val major: Int
        get() = rawVersion / 10000

    val minor: Int
        get() = rawVersion / 100 % 100

    val patch: Int
        get() = rawVersion % 100

    val classifier: Classifier? by lazy {
        rawClassifier?.let(Classifier::from)
    }

    val patchless: String
        get() = "$major.$minor"

    override val releaseTime: OffsetDateTime by lazy {
        val identifier = buildString {
            append(major).append('.').append(minor)
            if (patch != 0) {
                append('.').append(patch)
            }

            if (classifier != null) {
                var spacedRevision = false

                when (classifier) {
                    Classifier.PRE_RELEASE -> {
                        when (rawClassifier) {
                            "pre" -> {
                                append('-').append("pre")
                            }

                            "Pre-Release" -> {
                                spacedRevision = true
                                append(' ').append("Pre-Release")
                            }
                        }
                    }

                    else -> {
                        append('-').append(classifier!!.identifier.first())
                        revision?.let(::append)
                    }
                }

                if (revision != null) {
                    if (spacedRevision) {
                        append(' ')
                    }

                    append(revision)
                }
            }
        }

        MinecraftVersions.getReleaseTimeFor(identifier)
    }

    override val preprocessorKey: Int = rawVersion

    /**
     * Goes the whole way and compares the Minecraft version itself, the classifier and the revision.
     */
    override operator fun compareTo(other: MinecraftVersion<*>): Int {
        if (other !is MinecraftReleaseVersion) {
            return releaseTime.compareTo(other.releaseTime)
        }

        return when {
            rawVersion > other.rawVersion -> 1
            rawVersion < other.rawVersion -> -1
            classifier != null && other.classifier != null -> {
                val classifier = classifier!!
                val otherClassifier = other.classifier!!

                val classifierComparison = classifier.compareTo(otherClassifier)
                if (classifierComparison != 0) {
                    classifierComparison
                } else {
                    revision?.compareTo(other.revision ?: 1) ?: 0
                }
            }

            else -> 0
        }
    }

    /**
     * Simply compares the Minecraft release version to the given integer, useful for comparing versions to their raw values.
     * f.ex: `MinecraftVersions.VERSION_1_20_4 >= 1_20_01`
     */
    operator fun compareTo(other: Int): Int {
        return rawVersion.compareTo(other)
    }

    override fun equals(other: Any?): Boolean {
        if (other is MinecraftReleaseVersion) {
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
        result = 31 * result + (classifier?.hashCode() ?: 0)
        result = 31 * result + (revision ?: 0)
        return result
    }

    override fun toString(): String {
//        return "$major.$minor" + if (patch != 0) ".$patch" else ""
        return buildString {
            append(major).append('.').append(minor)
            if (patch != 0) {
                append('.').append(patch)
            }

            if (classifier != null) {
                var spacedRevision = false

                when (classifier) {
                    Classifier.PRE_RELEASE -> {
                        when (rawClassifier) {
                            "pre" -> {
                                append('-').append("pre")
                            }

                            "Pre-Release" -> {
                                spacedRevision = true
                                append(' ').append("Pre-Release")
                            }
                        }
                    }

                    else -> {
                        append('-').append(classifier!!.identifier.first())
                        revision?.let(::append)
                    }
                }

                if (revision != null) {
                    if (spacedRevision) {
                        append(' ')
                    }

                    append(revision)
                }
            }
        }
    }

}
