package dev.deftu.gradle.utils.mcinfo

import dev.deftu.gradle.utils.propertyIntOr
import dev.deftu.gradle.utils.version.MinecraftVersion
import dev.deftu.gradle.utils.version.MinecraftVersionMap
import org.gradle.api.Project

sealed class MinecraftInfo {

    companion object {

        private val revisions = listOf(
            MinecraftInfoV0,
            MinecraftInfoV1,
            MinecraftInfoV2
        )

        @JvmStatic
        fun get(project: Project): MinecraftInfo {
            val revision = project.propertyIntOr("minecraft.revision", default = 0, prefix = "dgt.")
            if (revision < 0) {
                throw IllegalArgumentException("MinecraftInfo revision cannot be negative: $revision")
            } else if (revision >= revisions.size) {
                throw IllegalArgumentException(
                    "MinecraftInfo revision $revision is not available. (Latest is ${revisions.size - 1}). " +
                            "Please update the toolkit to a newer version or check which revisions are available."
                )
            }
            return revisions[revision].also(MinecraftInfo::initialize)
        }

    }

    open var fabricLoaderVersion: String = "" // Default until we inherit or decide on our own
        protected set

    open var fabricLanguageKotlinVersion: String = "" // Default until we inherit or decide on our own
        protected set

    val fabricYarnVersions: MinecraftVersionMap<String> = MinecraftVersionMap()

    val fabricApiVersions: MinecraftVersionMap<String> = MinecraftVersionMap()

    val fabricModMenuDefinitions: MinecraftVersionMap<Pair<String, String>> = MinecraftVersionMap()

    val legacyFabricYarnVersions: MinecraftVersionMap<String> = MinecraftVersionMap()

    val legacyFabricApiVersions: MinecraftVersionMap<String> = MinecraftVersionMap()

    val kotlinForForgeVersions: MinecraftVersionMap<String> = MinecraftVersionMap()

    val forgeVersions: MinecraftVersionMap<String> = MinecraftVersionMap()

    val mcpDefinitions: MinecraftVersionMap<String> = MinecraftVersionMap()

    val neoForgeVersions: MinecraftVersionMap<String> = MinecraftVersionMap()

    val parchmentVersions: MinecraftVersionMap<String> = MinecraftVersionMap()

    /** updates versions as needed and/or inherits if needed */
    abstract fun initialize()

    /** inherits all maps and/or versions from the given info */
    fun inherit(other: MinecraftInfo) {
        other.initialize() // ensure the other is initialized

        this.fabricLoaderVersion = other.fabricLoaderVersion
        this.fabricLanguageKotlinVersion = other.fabricLanguageKotlinVersion
        this.fabricYarnVersions.putAll(other.fabricYarnVersions)
        this.fabricApiVersions.putAll(other.fabricApiVersions)
        this.fabricModMenuDefinitions.putAll(other.fabricModMenuDefinitions)
        this.legacyFabricYarnVersions.putAll(other.legacyFabricYarnVersions)
        this.legacyFabricApiVersions.putAll(other.legacyFabricApiVersions)
        this.kotlinForForgeVersions.putAll(other.kotlinForForgeVersions)
        this.forgeVersions.putAll(other.forgeVersions)
        this.mcpDefinitions.putAll(other.mcpDefinitions)
        this.neoForgeVersions.putAll(other.neoForgeVersions)
        this.parchmentVersions.putAll(other.parchmentVersions)
    }

    fun getFabricYarnVersion(version: MinecraftVersion<*>): String {
        return fabricYarnVersions[version]
            ?: throw IllegalArgumentException("No Fabric Yarn version found for $version")
    }

    fun getFabricApiVersion(version: MinecraftVersion<*>): String {
        return fabricApiVersions[version]
            ?: throw IllegalArgumentException("No Fabric API version found for $version")
    }

    fun getFabricModMenuDefinition(version: MinecraftVersion<*>): Pair<String, String> {
        return fabricModMenuDefinitions[version]
            ?: throw IllegalArgumentException("No Fabric Mod Menu definition found for $version")
    }

    fun getLegacyFabricYarnVersion(version: MinecraftVersion<*>): String {
        return legacyFabricYarnVersions[version]
            ?: throw IllegalArgumentException("No Legacy Fabric Yarn version found for $version")
    }

    fun getLegacyFabricApiVersion(version: MinecraftVersion<*>): String {
        return legacyFabricApiVersions[version]
            ?: throw IllegalArgumentException("No Legacy Fabric API version found for $version")
    }

    /**
     * we need to do a comparison here as we don't expect all versions to be filled out. rather,
     * it's a range, where we just match the closest version that is less than or equal to the given version.
     */
    fun getKotlinForForgeVersion(version: MinecraftVersion<*>): String {
        return kotlinForForgeVersions
            .filter { it.key <= version }
            .maxByOrNull { it.key }?.value
            ?: throw IllegalArgumentException("No Kotlin for Forge version found for $version")
    }

    fun getForgeVersion(version: MinecraftVersion<*>): String {
        return forgeVersions[version]
            ?: throw IllegalArgumentException("No Forge version found for $version")
    }

    fun getMcpDefinition(version: MinecraftVersion<*>): String {
        val definition = mcpDefinitions[version]
            ?: throw IllegalArgumentException("No MCP definition found for $version")
        return "de.oceanlabs.mcp:mcp_$definition"
    }

    fun getNeoForgeVersion(version: MinecraftVersion<*>): String {
        return neoForgeVersions[version]
            ?: throw IllegalArgumentException("No NeoForge version found for $version")
    }

    fun getParchmentVersion(version: MinecraftVersion<*>): String {
        return parchmentVersions[version]
            ?: throw IllegalArgumentException("No Parchment version found for $version")
    }

}
