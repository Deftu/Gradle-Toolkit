package dev.deftu.gradle.tools.jvm

import gradle.kotlin.dsl.accessors._855dbf577c54cab7c87165cfc8681ff2.dokkaJavadoc
import gradle.kotlin.dsl.accessors._8c47cae829ea3d03260d5ff13fb2398e.java

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
