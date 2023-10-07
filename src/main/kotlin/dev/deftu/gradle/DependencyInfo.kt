package dev.deftu.gradle

object DependencyInfo {
    val infoMap = mapOf(
        "kord" to "0.8.0-M17",
        "kordex" to "1.5.2-RC1",
        "jda" to "4.4.0_350",
        "jda-beta" to "5.0.0-beta.2",
        "sentry" to "6.11.0"
    )

    fun fetchKordVersion() = infoMap["kord"]!!
    fun fetchKordExVersion() = infoMap["kordex"]!!
    fun fetchJdaVersion() = infoMap["jda"]!!
    fun fetchJdaBetaVersion() = infoMap["jda-beta"]!!
    fun fetchSentryVersion() = infoMap["sentry"]!!
}
