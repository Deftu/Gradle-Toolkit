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
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.enhancedpixel.xyz/releases/")

    maven("https://jitpack.io/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.architectury.dev/")
    maven("https://repo.essential.gg/repository/maven-public")

    maven("https://maven.enhancedpixel.xyz/snapshots/")
    mavenLocal()
}

dependencies {
    // Language
    implementation(gradleApi())
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin"))

    // Architectury Loom
    implementation("gg.essential:architectury-loom:0.10.0.4")
    implementation("dev.architectury:architectury-pack200:0.1.3")

    // Preprocessing/multi-versioning
    implementation("com.github.replaymod:preprocessor:48e02ad")

    // Other
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.21")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("net.kyori:blossom:1.3.0")

    // Publishing
    implementation("com.modrinth.minotaur:Minotaur:2.4.2")
    implementation("gradle.plugin.com.matthewprenger:CurseGradle:1.4.0")
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
            mavenLocal()
            if (project.hasProperty("enhancedpixel.publishing.username") && project.hasProperty("enhancedpixel.publishing.password")) {
                fun MavenArtifactRepository.applyCredentials() {
                    authentication.create<BasicAuthentication>("basic")
                    credentials {
                        username = property("enhancedpixel.publishing.username")?.toString()
                        password = property("enhancedpixel.publishing.password")?.toString()
                    }
                }

                maven {
                    name = "EnhancedPixelReleases"
                    url = uri("https://maven.enhancedpixel.xyz/releases")
                    applyCredentials()
                }

                maven {
                    name = "EnhancedPixelSnapshots"
                    url = uri("https://maven.enhancedpixel.xyz/snapshots")
                    applyCredentials()
                }
            }
        }
    }
}
