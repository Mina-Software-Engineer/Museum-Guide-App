// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()

        //maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        val nav_version = "2.5.2"
        classpath("com.android.tools.build:gradle:7.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}


plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}