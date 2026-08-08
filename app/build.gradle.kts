plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Adding Compose Compiler Plugin without a hardcoded version, 
    // so it adapts to your root Kotlin version (2.0.21 as per your evidence).
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
        compose = true // Enabling Compose for the module
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
    implementation(project(":common"))
    implementation(project(":core-model"))
    implementation(project(":core-validation"))
    implementation(project(":core-intelligence"))
    implementation(project(":core-workflow"))
    implementation(project(":core-action"))
    implementation(project(":core-security"))
    implementation(project(":runtime"))
    implementation(project(":payment-engine"))

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity:1.10.1")

    testImplementation("junit:junit:4.13.2")

    // ⚠️ Jetpack Compose Standard Dependencies (No guessed versions. Using BOM)
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
}
