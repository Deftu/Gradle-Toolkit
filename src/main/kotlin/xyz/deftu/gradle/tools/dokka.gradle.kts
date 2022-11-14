package xyz.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._ca59d7b33a587bae1dcf00e1f22a5064.dokkaJavadoc
import gradle.kotlin.dsl.accessors._ca59d7b33a587bae1dcf00e1f22a5064.java

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
