import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URI

plugins {
    id("com.github.johnrengelman.shadow") version("7.1.2")
    `kotlin-dsl`
    `maven-publish`
}

val projectName: String by project
val projectVersion: String by project
val projectGroup: String by project

version = projectVersion
group = projectGroup

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.unifycraft.xyz/releases/")

    maven("https://jitpack.io/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.architectury.dev/")
    maven("https://repo.essential.gg/repository/maven-public/")

    maven("https://maven.unifycraft.xyz/snapshots/")
    mavenLocal()
}

dependencies {
    // Language
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")

    // Architectury Loom
    implementation("gg.essential:architectury-loom:0.10.0.4")
    implementation("dev.architectury:architectury-pack200:0.1.3")

    // Preprocessing/multi-versioning
    implementation("com.github.replaymod:preprocessor:48e02ad")

    // Other
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.7.0")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("net.kyori:blossom:1.3.1-UNIFYCRAFT")

    // Publishing
    implementation("com.modrinth.minotaur:Minotaur:2.3.1")
    implementation("me.hypherionmc.cursegradle:CurseGradle:2.0.1")
    implementation("com.github.breadmoirai:github-release:2.2.12")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    named<Jar>("jar") {
        archiveBaseName.set(projectName)
        from("LICENSE")
    }
}

afterEvaluate {
    publishing {
        repositories {
            if (project.hasProperty("unifycraft.publishing.username") && project.hasProperty("unifycraft.publishing.password")) {
                fun MavenArtifactRepository.applyCredentials() {
                    credentials {
                        username = property("unifycraft.publishing.username")?.toString()
                        password = property("unifycraft.publishing.password")?.toString()
                    }
                    authentication {
                        create<BasicAuthentication>("basic")
                    }
                }

                maven {
                    name = "UnifyCraftRelease"
                    url = URI.create("https://maven.unifycraft.xyz/releases")
                    applyCredentials()
                }

                maven {
                    name = "UnifyCraftSnapshots"
                    url = URI.create("https://maven.unifycraft.xyz/snapshots")
                    applyCredentials()
                }
            }
        }
    }
}
