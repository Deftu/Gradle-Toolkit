package dev.deftu.gradle.utils.version

import dev.deftu.gradle.utils.getMajorJavaVersion
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.time.OffsetDateTime

class MinecraftSnapshotVersion private constructor(
    override val javaVersion: JavaVersion,
    val year: Int,
    val week: Int,
    val revision: String,
) : MinecraftVersion<MinecraftSnapshotVersion> {

    companion object {

        @JvmStatic
        internal val regex = "(?<year>\\d+)w(?<week>\\d+)(?<revision>.+)".toRegex()

        @JvmStatic
        internal val parser: MinecraftVersionParser = "snapshot" to { project: Project?, version: String ->
            try {
                if (project == null) {
                    throw NullPointerException("Project is null")
                }

                val match = regex.find(version) ?: throw IllegalArgumentException("Invalid version format, could not match to regex: $version")
                val groups = match.groups

                val year = groups["year"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing year: $version")
                val week = groups["week"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing week: $version")
                val revision = groups["revision"]?.value ?: throw IllegalArgumentException("Invalid version format, missing revision: $version")

                val javaVersion = JavaVersion.toVersion(project.getMajorJavaVersion(false))
                VersionParseSuccess(MinecraftSnapshotVersion(javaVersion, year, week, revision))
            } catch (t: Throwable) {
                val message = t.message
                if (message == null) {
                    t.printStackTrace()
                }

                VersionParseError(message ?: "Unknown error")
            }
        }

        @JvmStatic
        fun from(javaVersion: JavaVersion, year: Int, week: Int, revision: String): MinecraftSnapshotVersion {
            return MinecraftSnapshotVersion(javaVersion, year, week, revision)
        }

        @JvmStatic
        fun from(project: Project, version: String): MinecraftSnapshotVersion {
            return parser.second(project, version).getOrThrow()
        }

    }

    override val releaseTime: OffsetDateTime by lazy {
        MinecraftVersions.getReleaseTimeFor(toString())
    }

    override val preprocessorKey: Int by lazy {
        (year * 10000) + (week * 100) + revisionToNumber()
    }

    /**
     * Alphabetical indexing approach
     */
    private fun revisionToNumber(): Int {
        return revision.fold(0) { acc, char -> acc + char.code }
    }

    override fun compareTo(other: MinecraftVersion<*>): Int {
        if (other !is MinecraftSnapshotVersion) {
            return releaseTime.compareTo(other.releaseTime)
        }

        return when {
            year != other.year -> year.compareTo(other.year)
            week != other.week -> week.compareTo(other.week)
            else -> revision.compareTo(other.revision)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is MinecraftSnapshotVersion) {
            return year == other.year && week == other.week && revision == other.revision
        }

        return false
    }

    override fun hashCode(): Int {
        var result = year.hashCode()
        result = 31 * result + week.hashCode()
        result = 31 * result + revision.hashCode()
        return result
    }

    /**
     * Returns a padded string of the snapshot version, f.ex: 25w01a
     */
    override fun toString(): String {
        return buildString {
            append(year)
            append("w")
            append(week.toString().padStart(2, '0'))
            append(revision)
        }
    }

}
