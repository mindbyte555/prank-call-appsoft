buildscript {

    dependencies {
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.8")
        classpath("com.google.gms:google-services:4.4.3")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.0")


    }
}
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("com.google.gms.google-services") version "4.4.3" apply false
    id("androidx.navigation.safeargs") version "2.5.3" apply false
}