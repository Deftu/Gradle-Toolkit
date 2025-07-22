package dev.deftu.gradle.tools.jvm

plugins {
    java
    id("org.jetbrains.dokka")
}

java {
    withJavadocJar()
}

tasks.named<Jar>("javadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc)
}
