package dev.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._0935894d714bf6b98fac60b9fc45a2f5.jar
import gradle.kotlin.dsl.accessors._0935894d714bf6b98fac60b9fc45a2f5.main
import gradle.kotlin.dsl.accessors._0935894d714bf6b98fac60b9fc45a2f5.sourceSets
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
