package dev.deftu.gradle.utils

class MinecraftVersionMap<V>(
    vararg pairs: Pair<MinecraftVersion, V>
) : HashMap<MinecraftVersion, V>(pairs.toMap()) {

    constructor() : this(*emptyArray())

    /**
     * We override get to ensure that we can get the appropriate value, seeing as MinecraftVersion can have different instances for the same version.
     */
    override operator fun get(key: MinecraftVersion): V? {
        return super.entries.firstOrNull { (version, _) ->
            version == key
        }?.value
    }

}
