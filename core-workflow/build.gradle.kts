plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":common"))
    implementation(project(":core-model"))
    implementation(project(":core-validation"))
    
    // These are removed for now as they are not used by the current WorkflowController
    // implementation(project(":core-intelligence"))
    // implementation(project(":core-action"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
