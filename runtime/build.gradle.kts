import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vmax.runtime"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {

    implementation(project(":common"))
    implementation(project(":core-model"))
    implementation(project(":core-validation"))
    implementation(project(":core-intelligence"))
    implementation(project(":core-action"))
    implementation(project(":core-workflow"))

    // Android / Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    // Google ML Kit - Text Recognition
    implementation("com.google.mlkit:text-recognition:16.0.1")

    testImplementation(kotlin("test"))
}
