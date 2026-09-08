package dev.deftu.gradle.utils

enum class ModLoader(
    val friendlyName: String,
    vararg aliases: String,
) {
    NEOFORGE("NeoForge"),
    FORGE("Forge"),
    FABRIC("Fabric", "ornithe"),
    OTHER("Unknown");

    val friendlyString: String
        get() = name.lowercase()

    val identifiers: List<String> = listOf(name.lowercase()) + aliases.map(String::lowercase)

    override fun toString(): String {
        return friendlyString
    }

    companion object {

        @JvmStatic
        fun from(value: String): ModLoader {
            return when {
                value.contains("neoforge", ignoreCase = true) || value.contains("neoforged", ignoreCase = true) -> NEOFORGE
                value.contains("forge", ignoreCase = true) -> FORGE
                value.contains("fabric", ignoreCase = true) || value.contains("ornithe", ignoreCase = true) -> FABRIC
                else -> OTHER
            }
        }

        @JvmStatic
        fun isLoaderProjectName(value: String): Boolean {
            return values().any { loader ->
                loader.identifiers.any { identifier ->
                    value.endsWith("-$identifier", true)
                }
            }
        }

    }

}
