plugins {
    kotlin("jvm") version("2.3.0")
    `kotlin-dsl`
    val dgtVersion = "2.70.0"
    id("dev.deftu.gradle.tools.repo") version(dgtVersion)
    id("dev.deftu.gradle.tools.configure") version(dgtVersion)
    id("dev.deftu.gradle.tools.publishing.maven") version(dgtVersion)
    id("dev.deftu.gradle.tools.publishing.github") version(dgtVersion)
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${property("kotlin.version")}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("kotlin.version")}")

    // Architectury Loom
    implementation("gg.essential:architectury-loom:1.13.44")
    implementation("dev.architectury:architectury-pack200:0.1.3")

    // Preprocessing/multi-versioning
    implementation("dev.deftu:preprocessor:0.16.0")

    // Documentation
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.0.0")
    implementation("org.jetbrains.dokka:dokka-base:2.0.0")

    // Publishing
    implementation("com.modrinth.minotaur:Minotaur:2.8.7")
    implementation("net.darkhax.curseforgegradle:CurseForgeGradle:1.1.26")
    implementation("com.github.breadmoirai:github-release:2.5.2")

    // Other
    implementation("com.gradleup.shadow:com.gradleup.shadow.gradle.plugin:9.4.0")
    implementation("dev.deftu:Bloom:0.2.0")
}

tasks {
    named<Jar>("jar") {
        from("LICENSE")
    }
}
