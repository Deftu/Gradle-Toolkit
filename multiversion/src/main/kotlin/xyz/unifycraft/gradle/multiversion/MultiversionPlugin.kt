package xyz.unifycraft.gradle.multiversion

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.LoomGradlePlugin
import net.fabricmc.loom.configuration.providers.forge.fg2.Pack200Provider
import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import xyz.unifycraft.gradle.base.BasePlugin

class MultiversionPlugin : BasePlugin() {
    override fun onApply(project: Project) {
        val extension = project.extensions.create("multiversion", MultiversionExtension::class.java, project)
        val mcVersion = extension.mcVersion.getOrElse(MCVersion.from(project))

        project.tasks.create("clearMappingsCache") {
            it.group = "multiversion"
            dependenciesJsonCache = fetchDependenciesJson()
        }

        val isLoomPresent = project.plugins.hasPlugin(LoomGradlePlugin::class.java as Class<out (Plugin<Project>)>)
        val isArchLoomPresent = try {
            Class.forName("net.fabricmc.loom.api.ForgeExtensionAPI")
            true
        } catch (e: Exception) {
            false
        }

        if (isLoomPresent) {
            if (
                !project.extensions.extraProperties.has("loom.platform") &&
                mcVersion.isForge
            ) project.extensions.extraProperties["loom.platform"] = mcVersion.loader.toString().lowercase()
            assignDependency(project, "net.minecraft:minecraft:${mcVersion.versionStr}", "minecraft", extension.assignMinecraftDependency)
            setupMappingsDependency(project, extension, mcVersion)
            if (mcVersion.isFabric)
                setupFabricLoaderDependency(project, extension, mcVersion)
            if (isArchLoomPresent && mcVersion.isForge)
                setupForgeDependency(project, extension, mcVersion)
        }
    }

    private fun setupMappingsDependency(project: Project, extension: MultiversionExtension, mcVersion: MCVersion) {
        val dependencies = project.provider {
            dependenciesJsonCache ?: fetchDependenciesJson().also {
                dependenciesJsonCache = it
            }
        }

        if (dependencies.isPresent) {
            val loaderObject = dependencies.get().getAsJsonObject(mcVersion.loader.toString().lowercase())
            val mappingDependency = loaderObject["mappings"]?.asString ?: throw MinecraftVersionException("Unable to find mappings dependency for Minecraft version ${mcVersion.versionStr}")
            if (mappingDependency != "official") {
                assignDependency(project, mappingDependency, "mappings", extension.assignMappingsDependency)
            } else {
                val loomExtension = project.extensions.getByType(LoomGradleExtension::class.java)
                project.dependencies.add("mappings", loomExtension.officialMojangMappings())
                //loomExtension.forge.pack200Provider.set(Pack200A)
            }
        } else project.logger.warn("Unable to find mappings for ${mcVersion.versionStr}.")
    }

    private fun setupFabricLoaderDependency(project: Project, extension: MultiversionExtension, mcVersion: MCVersion) {
        val dependencies = project.provider {
            dependenciesJsonCache ?: fetchDependenciesJson().also {
                dependenciesJsonCache = it
            }
        }

        if (dependencies.isPresent) {
            val loaderObject = dependencies.get().getAsJsonObject(mcVersion.loader.toString().lowercase())
            val loaderDependency = loaderObject["loader"]?.asString ?: throw MinecraftVersionException("Unable to find loader dependency for Minecraft version ${mcVersion.versionStr}")
            assignDependency(project, loaderDependency, "modImplementation", extension.assignMappingsDependency)
        } else project.logger.warn("Unable to find Fabric loader for ${mcVersion.versionStr}.")
    }

    private fun setupForgeDependency(project: Project, extension: MultiversionExtension, mcVersion: MCVersion) {
        val dependencies = project.provider {
            dependenciesJsonCache ?: fetchDependenciesJson().also {
                dependenciesJsonCache = it
            }
        }

        if (dependencies.isPresent) {
            val loaderObject = dependencies.get().getAsJsonObject(mcVersion.loader.toString().lowercase())
            val forgeDependency = loaderObject["forge"]?.asString ?: throw MinecraftVersionException("Unable to find forge dependency for Minecraft version ${mcVersion.versionStr}")
            assignDependency(project, forgeDependency, "forge", extension.assignMappingsDependency)
        } else project.logger.warn("Unable to find forge for ${mcVersion.versionStr}.")
    }

    private fun assignDependency(
        project: Project,
        dependency: String,
        configuration: String,
        check: Provider<Boolean>
    ) {
        if (check.getOrElse(false)) {
            project.dependencies.add(configuration, dependency)
        }
    }

    companion object {
        const val DEPENDENCIES_URL = "https://raw.githubusercontent.com/UnifyCraft/Gradle-Toolkit/main/multiversion/dependencies.json"
        private val httpClient = OkHttpClient.Builder()
            .addInterceptor {
                it.proceed(it.request().newBuilder()
                    .addHeader("User-Agent", "UnifyCraft Multiversion/1.0.0")
                    .build())
            }.build()
        private var dependenciesJsonCache: JsonObject? = null
        fun fetchDependenciesJson(): JsonObject {
            val response = httpClient.newCall(Request.Builder()
                .url(DEPENDENCIES_URL)
                .get()
                .build()).execute()
            return JsonParser.parseString(response.body?.string()).asJsonObject
        }
    }
}