package xyz.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._8eaad88f99c255f0ca90801b5b69f5d7.dokkaJavadoc
import gradle.kotlin.dsl.accessors._8eaad88f99c255f0ca90801b5b69f5d7.java

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
