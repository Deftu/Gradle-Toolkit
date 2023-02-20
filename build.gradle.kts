plugins {
    kotlin("jvm") version("1.6.21")
    `kotlin-dsl`
    `maven-publish`
}

val projectName: String by project
val projectVersion: String by project
val projectGroup: String by project

version = projectVersion
group = projectGroup

repositories {
    maven("https://jitpack.io/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.architectury.dev/")
    maven("https://repo.essential.gg/repository/maven-public/")

    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
}

dependencies {
    // Language
    implementation(gradleApi())
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin"))

    // Architectury Loom
    implementation("gg.essential:architectury-loom:0.10.0.5")
    implementation("dev.architectury:architectury-pack200:0.1.3")

    // Preprocessing/multi-versioning
    implementation("com.github.replaymod:preprocessor:f14e81e")

    // Other
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.21")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("net.kyori:blossom:1.3.0")

    // Publishing
    implementation("com.modrinth.minotaur:Minotaur:2.7.2")
    implementation("net.darkhax.curseforgegradle:CurseForgeGradle:1.0.11")
    implementation("com.github.breadmoirai:github-release:2.4.1")
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
            mavenLocal()
            if (project.hasProperty("deftu.publishing.username") && project.hasProperty("deftu.publishing.password")) {
                fun MavenArtifactRepository.applyCredentials() {
                    authentication.create<BasicAuthentication>("basic")
                    credentials {
                        username = property("deftu.publishing.username")?.toString()
                        password = property("deftu.publishing.password")?.toString()
                    }
                }

                maven {
                    name = "DeftuReleases"
                    url = uri("https://maven.deftu.xyz/releases")
                    applyCredentials()
                }

                maven {
                    name = "DeftuSnapshots"
                    url = uri("https://maven.deftu.xyz/snapshots")
                    applyCredentials()
                }
            }
        }
    }
}
