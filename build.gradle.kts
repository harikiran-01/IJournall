// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.dagger.hilt.android") version "2.42" apply false

    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(BuildPlugins.gradle)
        classpath(BuildPlugins.kotlin)
        classpath(BuildPlugins.safeArgs)
        classpath(BuildPlugins.hilt)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
