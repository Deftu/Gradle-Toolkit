pluginManagement {
    repositories {
        // Snapshots
        mavenLocal()
        maven("https://maven.deftu.dev/snapshots")

        // Repositories
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://jitpack.io/")

        // Default repositories
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = extra["project.name"]!!.toString()
