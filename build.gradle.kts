// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlin_version by extra("1.9.22")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath ("com.vanniktech:gradle-maven-publish-plugin:0.18.0")
        //monitor-plugin需要
        classpath ("io.github.lygttpod:monitor-plugin:0.0.2")
        classpath ("com.android.tools.build:gradle:8.2.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        //maven { url; "https://jitpack.io" }
    }
}

