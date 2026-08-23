plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // यह लाइन जोड़ना बहुत जरूरी है, क्योंकि Logger यहाँ से आता है
    implementation(project(":common"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
