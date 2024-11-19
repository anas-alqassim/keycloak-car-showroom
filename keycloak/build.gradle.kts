plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("org.sonarqube") version "5.1.0.4882"
}

group = "com.elm.challenge"
version = "0.0.1"

repositories {
    mavenCentral()
}
val keycloakVersion = "25.0.2"
val kotlinLoggingVersion = "3.0.5"
dependencies {
    compileOnly("org.keycloak:keycloak-server-spi-private:25.0.1")
    compileOnly("org.keycloak:keycloak-server-spi:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-services:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-model-jpa:$keycloakVersion")
    implementation(kotlin("stdlib"))
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    destinationDirectory.set(file("../providers"))
    outputs.upToDateWhen { false }
}

tasks.detekt {
    this.jvmTarget = "17"
    exclude(".gradle/", ".build/", ".idea/")
    config.from("detekt.yml")
    buildUponDefaultConfig = true
}


