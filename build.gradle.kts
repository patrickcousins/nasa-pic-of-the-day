// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
    //kotlin("jvm") version "1.9.21" apply false
    //"1.9.0-1.0.12"
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("androidx.navigation.safeargs") version "2.7.6" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.6" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.7.6"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}