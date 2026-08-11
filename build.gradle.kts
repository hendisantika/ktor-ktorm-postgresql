val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val ktorm_version: String by project
val postgresql_driver_version: String by project

val junit_version: String by project
val testcontainers_version: String by project

plugins {
    kotlin("jvm") version "2.4.10"
    id("io.ktor.plugin") version "3.5.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.10"
}

group = "id.my.hendisantika"
version = "0.0.1"

application {
    mainClass = "id.my.hendisantika.ApplicationKt"
}

kotlin {
    // Keep local builds and CI (JDK 25) producing identical bytecode.
    jvmToolchain(25)
}

ktor {
    docker {
        // The fat JAR is Java 25 bytecode, so the image needs a matching JRE.
        jreVersion = JavaVersion.VERSION_25
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("org.ktorm:ktorm-core:$ktorm_version")
    implementation("org.ktorm:ktorm-support-postgresql:$ktorm_version")
    implementation("org.postgresql:postgresql:$postgresql_driver_version")

    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")

    // JUnit 5
    testImplementation(platform("org.junit:junit-bom:$junit_version"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Testcontainers
    testImplementation(platform("org.testcontainers:testcontainers-bom:$testcontainers_version"))
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
