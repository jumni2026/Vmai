dependencies {
    implementation(project(":common"))
    implementation(project(":core-model"))
    implementation(project(":core-validation"))
    implementation(project(":core-intelligence"))
    implementation(project(":core-action"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    testImplementation(kotlin("test"))
}
