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

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
