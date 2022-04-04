package xyz.unifycraft.gradle.unibuild

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

abstract class UniBuildExtension {
    // Automatic dependents.
    abstract val dependantJar: Property<Boolean>

    // Git
    abstract val useGitBranch: Property<Boolean>
    abstract val useGitHash: Property<Boolean>
    abstract val gitBranchExclusions: ListProperty<String>
    abstract val gitBranchName: Property<String>
    abstract val gitBranch: Property<String>
    abstract val gitHashName: Property<String>
    abstract val gitHash: Property<String>

    // Builds
    abstract val useBuildNumber: Property<Boolean>
    abstract val buildNumberName: Property<String>
    abstract val buildNumber: Property<String>

    // Minecraft
    abstract val mcVersion: Property<String>
    abstract val mcPlatform: Property<String>

    init {
        dependantJar.convention(true)

        useGitBranch.convention(USE_GIT_BRANCH_DEFAULT)
        useGitHash.convention(USE_GIT_HASH_DEFAULT)
        gitBranchExclusions.convention(GIT_BRANCH_EXCLUSIONS_DEFAULT)
        gitBranchName.convention(GIT_BRANCH_NAME_DEFAULT)
        gitBranch.forUseAtConfigurationTime()
        gitHashName.convention(GIT_HASH_NAME_DEFAULT)
        gitHash.forUseAtConfigurationTime()

        useBuildNumber.convention(USE_BUILD_NUMBER_DEFAULT)
        buildNumberName.convention(BUILD_NUMBER_NAME_DEFAULT)
        buildNumber.forUseAtConfigurationTime()

        mcVersion.forUseAtConfigurationTime()
        mcPlatform.forUseAtConfigurationTime()
    }

    fun usesGitBranch() =
        useGitBranch.get() && (gitBranchName.isPresent || gitBranch.isPresent)
    fun usesGitHash() =
        useGitHash.get() && (gitHashName.isPresent || gitHash.isPresent)

    fun usesBuildNumber() =
        useBuildNumber.get() && (buildNumberName.isPresent || buildNumber.isPresent)

    fun hasMcInfo() =
        mcVersion.isPresent && mcPlatform.isPresent

    companion object {
        const val USE_GIT_BRANCH_DEFAULT = true
        const val USE_GIT_HASH_DEFAULT = true
        @JvmStatic val GIT_BRANCH_EXCLUSIONS_DEFAULT = listOf("main", "master")
        const val GIT_BRANCH_NAME_DEFAULT = "GIT_BRANCH"
        const val GIT_HASH_NAME_DEFAULT = "GIT_COMMIT"

        const val USE_BUILD_NUMBER_DEFAULT = false
        const val BUILD_NUMBER_NAME_DEFAULT = "BUILD_NUMBER"
    }
}