package dev.deftu.gradle.tools

import gradle.kotlin.dsl.accessors._0935894d714bf6b98fac60b9fc45a2f5.main
import gradle.kotlin.dsl.accessors._0935894d714bf6b98fac60b9fc45a2f5.sourceSets
import org.gradle.kotlin.dsl.java

plugins {
    java
}

sourceSets {
    val ducks by creating
    main {
        ducks.compileClasspath += compileClasspath
        compileClasspath += ducks.output
    }
}
