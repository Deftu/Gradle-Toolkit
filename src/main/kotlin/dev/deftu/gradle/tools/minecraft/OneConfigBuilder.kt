package dev.deftu.gradle.tools.minecraft

class OneConfigBuilder {

    var version: String? = null
    var loaderVersion: String? = null

    var usePolyMixin = false
    var polyMixinVersion: String? = null

    var applyLoaderTweaker = true

    internal val modules = mutableSetOf<String>()

    operator fun String.unaryPlus() {
        modules.add(this)
    }

    operator fun String.unaryMinus() {
        modules.remove(this)
    }

    fun addModule(module: String) {
        modules.add(module)
    }

    fun removeModule(module: String) {
        modules.remove(module)
    }

}
