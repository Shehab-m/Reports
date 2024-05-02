//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    alias(libs.plugins.androidApplication) apply false
//    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
//}

buildscript {
    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.10")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.android.library") version "7.4.2" apply false

    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
//    id("org.jetbrains.kotlin.jvm") version "1.8.10" apply false
    kotlin("plugin.serialization") version "1.8.10"
//    kotlin("android") version "1.8.10" apply false
}
