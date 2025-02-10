package dev.deftu.gradle.tools

import org.gradle.kotlin.dsl.java

plugins {
    java
}

sourceSets {
    val ducks by creating {
        // Use the output of the `main` source set without modifying its classpath
        compileClasspath += sourceSets["main"].compileClasspath
    }

    main {
        // Include the output of the `ducks` source set in the compile classpaths
        compileClasspath += ducks.output
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
