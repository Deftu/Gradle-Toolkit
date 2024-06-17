package dev.deftu.gradle.utils

enum class ModLoader(
    val friendlyName: String,
    val loomPlatform: String
) {
    NEOFORGED("NeoForge", "neoforge"),
    FORGE("Forge", "forge"),
    FABRIC("Fabric", "fabric"),
    OTHER("Unknown", "");

    val friendlyString: String
        get() = name.lowercase()

    override fun toString(): String {
        return friendlyString
    }

    companion object {

        @JvmStatic
        fun from(value: String): ModLoader {
            return when {
                value.contains("neoforge", ignoreCase = true) || value.contains("neoforged", ignoreCase = true) -> NEOFORGED
                value.contains("forge", ignoreCase = true) -> FORGE
                value.contains("fabric", ignoreCase = true) -> FABRIC
                else -> OTHER
            }
        }

    }

}
