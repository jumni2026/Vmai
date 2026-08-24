plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // core-security के लिए आवश्यक मॉड्यूल्स
    implementation(project(":common"))
    implementation(project(":core-model"))
    implementation(project(":core-validation"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
