import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    `kotlin-dsl`
    val dgtVersion = "2.35.0"
    id("dev.deftu.gradle.tools.repo") version(dgtVersion)
    id("dev.deftu.gradle.tools.configure") version(dgtVersion)
    id("dev.deftu.gradle.tools.publishing.maven") version(dgtVersion)
    id("dev.deftu.gradle.tools.publishing.github") version(dgtVersion)
}

logger.lifecycle("> Kotlin compiler version: ${KotlinCompilerVersion.VERSION}")

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
    implementation("gg.essential:architectury-loom:1.9.31")
    implementation("dev.architectury:architectury-pack200:0.1.3")

    // Preprocessing/multi-versioning
    implementation("dev.deftu:preprocessor:0.10.0")

    // Documentation
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.0.0")
    implementation("org.jetbrains.dokka:dokka-base:2.0.0")

    // Publishing
    implementation("com.modrinth.minotaur:Minotaur:2.8.7")
    implementation("net.darkhax.curseforgegradle:CurseForgeGradle:1.1.26")
    implementation("com.github.breadmoirai:github-release:2.5.2")

    // Other
    implementation("com.github.johnrengelman:shadow:8.1.1")
    implementation("dev.deftu:Bloom:0.1.2")
}

tasks {
    named<Jar>("jar") {
        from("LICENSE")
    }
}
