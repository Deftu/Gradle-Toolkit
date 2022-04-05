package xyz.unifycraft.gradle.multiversion

import org.gradle.api.Project
import xyz.unifycraft.gradle.base.BasePlugin
import xyz.unifycraft.gradle.loomconfig.LoomConfigExtension
import xyz.unifycraft.gradle.loomconfig.LoomConfigPlugin

class MultiversionPlugin : BasePlugin() {
    override fun onApply(project: Project) {
        val extension = project.extensions.create("multiversion", MultiversionExtension::class.java, project)
        val mcVersion = extension.mcVersion.getOrElse(MCVersion.from(project))
        project.extensions.extraProperties.set("mcVersion", mcVersion)

        // Automatically configure the Loom Config plugin
        // so that it uses the correct information.
        if (project.plugins.hasPlugin(LoomConfigPlugin::class.java)) {
            val loomConfigExtension = project.extensions.getByType(LoomConfigExtension::class.java)
            loomConfigExtension.forge.set(mcVersion.isForge)
            loomConfigExtension.version.set(mcVersion.versionStr)
            val loomConfigPlugin = project.plugins.getPlugin(LoomConfigPlugin::class.java)
            loomConfigPlugin.setup(project, loomConfigExtension, loomConfigExtension.version.get())
        }
    }
}