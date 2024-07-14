plugins {
    kotlin("jvm") version("2.0.0")
    `kotlin-dsl`
    val dgt = "2.0.9"
    id("dev.deftu.gradle.tools.repo") version(dgt)
    id("dev.deftu.gradle.tools.configure") version(dgt)
    id("dev.deftu.gradle.tools.publishing.maven") version(dgt)
    id("dev.deftu.gradle.tools.publishing.github") version(dgt)
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
    implementation("gg.essential:architectury-loom:1.6.17")
    implementation("dev.architectury:architectury-pack200:0.1.3")

    // Preprocessing/multi-versioning
    implementation("dev.deftu:preprocessor:0.6.0")

    // Documentation
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.9.20")
    implementation("org.jetbrains.dokka:dokka-base:1.9.20")

    // Publishing
    implementation("com.modrinth.minotaur:Minotaur:2.8.7")
    implementation("net.darkhax.curseforgegradle:CurseForgeGradle:1.1.17")
    implementation("com.github.breadmoirai:github-release:2.4.1")

    // Other
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("dev.deftu:Bloom:0.1.2")
}

tasks {
    named<Jar>("jar") {
        from("LICENSE")
    }
}
