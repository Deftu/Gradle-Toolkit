plugins {
    kotlin("jvm") version("1.9.0")
    `kotlin-dsl`
    val dgt = "1.18.7+"
    id("xyz.deftu.gradle.tools.repo") version(dgt)
    id("xyz.deftu.gradle.tools.configure") version(dgt)
    id("xyz.deftu.gradle.tools.maven-publishing") version(dgt)
    id("xyz.deftu.gradle.tools.github-publishing") version(dgt)
}

toolkitGitHubPublishing {
    owner.set("Deftu")
    repository.set("Gradle-Toolkit")
    automaticallyGenerateReleaseNotes.set(true)
    useSourcesJar.set(true)
}

toolkitMavenPublishing {
    setupPublication.set(false)
}

repositories {
    maven("https://jitpack.io/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.jab125.dev/")

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
    implementation("gg.essential:architectury-loom:1.3.11")

    // Preprocessing/multi-versioning
    implementation("xyz.deftu:preprocessor:0.3.0")

    // Other
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.21")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("net.kyori:blossom:1.3.1")

    // Publishing
    implementation("com.modrinth.minotaur:Minotaur:2.8.2")
    implementation("net.darkhax.curseforgegradle:CurseForgeGradle:1.0.11")
    implementation("com.github.breadmoirai:github-release:2.4.1")
}

tasks {
    named<Jar>("jar") {
        from("LICENSE")
    }
}
