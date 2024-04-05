plugins {
    kotlin("jvm") version("1.9.10")
    `kotlin-dsl`
    val dgt = "1.21.4"
    id("dev.deftu.gradle.tools.repo") version(dgt)
    id("dev.deftu.gradle.tools.configure") version(dgt)
    id("dev.deftu.gradle.tools.maven-publishing") version(dgt)
    id("dev.deftu.gradle.tools.github-publishing") version(dgt)
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
    implementation("gg.essential:architectury-loom:1.3.12")
    implementation("dev.architectury:architectury-pack200:0.1.3")

    // Preprocessing/multi-versioning
    implementation("dev.deftu:preprocessor:0.4.2")

    // Documentation
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.9.10")
    implementation("org.jetbrains.dokka:dokka-base:1.9.10")

    // Publishing
    implementation("com.modrinth.minotaur:Minotaur:2.8.7")
    implementation("net.darkhax.curseforgegradle:CurseForgeGradle:1.1.17")
    implementation("com.github.breadmoirai:github-release:2.4.1")

    // Other
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("net.kyori:blossom:1.3.1")
}

tasks {
    named<Jar>("jar") {
        from("LICENSE")
    }
}
