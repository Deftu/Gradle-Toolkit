import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URI

plugins {
    id("com.github.johnrengelman.shadow") version("7.1.2")
    `kotlin-dsl`
    `maven-publish`
    signing
}

val projectName: String by project
val projectDescription: String by project
val projectUrl: String by project
val projectScm: String by project
val projectVersion: String by project
val projectGroup: String by project

version = projectVersion
group = projectGroup

repositories {
    mavenCentral()
    gradlePluginPortal()

    maven("https://jitpack.io/")
    maven("https://maven.fabricmc.net")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.architectury.dev/")
    maven("https://repo.essential.gg/repository/maven-public")
    maven("https://server.bbkr.space/artifactory/libs-release/")

    mavenLocal()
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    // Language
    implementation(gradleApi())
    shade("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")

    // Architectury Loom
    implementation("gg.essential:architectury-loom:0.10.0.3")
    implementation("dev.architectury:architectury-pack200:0.1.3")

    // Preprocessing/multi-versioning
    implementation("com.github.replaymod:preprocessor:73d8bed")

    // Publishing
    implementation("com.modrinth.minotaur:Minotaur:2.1.1")
    implementation("gradle.plugin.com.matthewprenger:CurseGradle:1.4.0")
    implementation("com.github.breadmoirai:github-release:2.2.12")

    // Other
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("net.kyori:blossom:1.3.0")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    named<Jar>("jar") {
        archiveBaseName.set(projectName)
        dependsOn("shadowJar")
        from("LICENSE")
    }

    named<ShadowJar>("shadowJar") {
        configurations = listOf(shade)
        archiveClassifier.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

afterEvaluate {
    publishing {
        publications.filterIsInstance<MavenPublication>().forEach { publication ->
            publication.pom {
                name.set(projectName)
                description.set(projectDescription)
                url.set(projectUrl)

                scm {
                    url.set(projectScm)
                }

                developers {
                    developer {
                        id.set("unifycraft")
                        name.set("UnifyCraft Team")
                        email.set("contact@unifycraft.xyz")
                    }
                }

                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.en.html")
                    }
                }
            }
        }

        repositories {
            if (project.hasProperty("unifycraft.publishing.username") && project.hasProperty("unifycraft.publishing.password")) {
                maven {
                    name = "MavenCentral"
                    url = URI.create("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = property("unifycraft.publishing.username")?.toString()
                        password = property("unifycraft.publishing.password")?.toString()
                    }
                }
            }
        }
    }

    if (project.hasProperty("signing.password")) {
        signing {
            publishing.publications.forEach(::sign)
            sign(configurations.archives.get())
        }
    }
}
