package dev.deftu.gradle.utils

enum class IntermediarySource(
    val providerName: String,
    val urlTemplate: String,
) {
    LEGACY_FABRIC_V1(
        "legacy-intermediary-1-v2",
        "$LEGACY_FABRIC_MAVEN/net/legacyfabric/intermediary/%1\$s/intermediary-%1\$s-v2.jar"
    ),
    LEGACY_FABRIC_V2(
        "legacy-intermediary-2-v2",
        "$LEGACY_FABRIC_MAVEN/net/legacyfabric/v2/intermediary/%1\$s/intermediary-%1\$s-v2.jar"
    ),
    CALAMUS_GEN2(
        "calamus-intermediary-gen2-v2",
        "$ORNITHE_MAVEN/net/ornithemc/calamus-intermediary-gen2/%1\$s/calamus-intermediary-gen2-%1\$s-v2.jar"
    );

    companion object {

        @JvmStatic
        fun fromLegacyFabricVersion(version: Int): IntermediarySource {
            return when (version) {
                2 -> LEGACY_FABRIC_V2
                else -> LEGACY_FABRIC_V1
            }
        }

    }

}
