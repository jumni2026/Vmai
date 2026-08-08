plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // ✅ Compose Compiler Plugin added without version. 
    // It will automatically resolve from the Root build.gradle.kts (Kotlin 2.0.21 environment).
    id("org.jetbrains.kotlin.plugin.compose") 
}

android {
    namespace = "com.vmax.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vmax.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "2.6"
    }

    buildFeatures {
        buildConfig = true
        // ✅ Enables Jetpack Compose for this module
        compose = true 
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Internal Modules
    implementation(project(":common"))
    implementation(project(":core-model"))
    implementation(project(":core-validation"))
    implementation(project(":core-intelligence"))
    implementation(project(":core-workflow"))
    implementation(project(":core-action"))
    implementation(project(":core-security"))
    implementation(project(":runtime"))
    implementation(project(":payment-engine"))

    // AndroidX Basics
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity:1.10.1")
    testImplementation("junit:junit:4.13.2")

    // ✅ Jetpack Compose Libraries (No Guessed BOM Versions. Hardcoded to match Root 2.0.21 setup)
    implementation("androidx.compose.ui:ui:1.7.0")
    implementation("androidx.compose.ui:ui-graphics:1.7.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.0")
    implementation("androidx.compose.material3:material3:1.3.0")
}
