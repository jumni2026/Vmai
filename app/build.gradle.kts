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
    // ✅ केवल यह एक लाइन जोड़नी है (बाकी Compose BOM पुराना ही रहेगा)
    implementation("androidx.activity:activity-compose:1.10.1") 
    testImplementation("junit:junit:4.13.2")

    // Jetpack Compose Libraries (BOM version 2024.02.00 को ही रखना है, बदलना नहीं)
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
}
