package xyz.unifycraft.gradle.tools

import gradle.kotlin.dsl.accessors._06e55093d2b2796fa8ca19eb1df48cd4.archives
import gradle.kotlin.dsl.accessors._06e55093d2b2796fa8ca19eb1df48cd4.java
import gradle.kotlin.dsl.accessors._ca59d7b33a587bae1dcf00e1f22a5064.dokkaJavadoc

plugins {
    java
    id("org.jetbrains.dokka")
}

java {
    withJavadocJar()
}

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("dokka-javadoc")
}

artifacts {
    archives(tasks.named("javadocJar"))
    archives(dokkaJavadocJar)
}
