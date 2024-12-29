package dev.deftu.gradle.tools.minecraft

/**
 * Builds a configuration for adding OneConfig and it's modules to your project.
 *
 * It can be used with the `useOneConfig` function in the Loom helper extension, like so:
 * ```kt
 * useOneConfig {
 *     // OneConfig configuration
 *     version = "1.0.0-alpha.47"
 *     loaderVersion = "1.1.0-alpha.34"
 *
 *     // Uses PolyMixin for Forge 1.8.9 & 1.12.2
 *     usePolyMixin = true
 *     polyMixinVersion = "0.8.4+build.2"
 *
 *     // Disable the OneConfig tweaker if you want to use your own
 *     applyLoaderTweaker = false
 *
 *     // Add modules
 *     +"config"
 *     +"commands"
 *     +"events"
 * }
 * ```
 *
 * Appending modules is done via Kotlin's unary plus feature, and removing modules is done via unary minus. Meaning that you can simply add or remove modules with the `+` and `-` operators.
 * If you would rather not use this feature, you can use the `addModule` and `removeModule` functions.
 */
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
