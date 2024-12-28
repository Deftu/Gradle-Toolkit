package dev.deftu.gradle.tools.minecraft

class OneConfigBuilder {

    var version: String? = null
    var loaderVersion: String? = null

    var usePolyMixin = false
    var polyMixinVersion: String? = null

    internal val modules = setOf<String>()

    operator fun String.unaryPlus() {
        modules.plus(this)
    }

    operator fun String.unaryMinus() {
        modules.minus(this)
    }

}
