
plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":common"))
    implementation(project(":core-model"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
