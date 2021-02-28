import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "my.little"
version = "0.0.1"

repositories {
    jcenter()
    mavenCentral()
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("org.jlleitschuh.gradle.ktlint")
}

val ktorVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val hikariVersion: String by project
val postgresVersion: String by project
val flywayVersion: String by project
val jupiterVersion: String by project
val testContainersVersion: String by project
val mockkVersion: String by project

dependencies {
    implementation("io.ktor", "ktor-server-netty", ktorVersion)
    implementation("io.ktor", "ktor-auth-jwt", ktorVersion)
    implementation("io.ktor", "ktor-server-sessions", ktorVersion)
    implementation("io.ktor", "ktor-serialization", ktorVersion)
    implementation("ch.qos.logback", "logback-classic", logbackVersion)

    // Database
    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
    implementation("com.zaxxer", "HikariCP", hikariVersion)
    implementation("org.flywaydb", "flyway-core", flywayVersion)
    runtimeOnly("org.postgresql", "postgresql", postgresVersion)

    testImplementation("io.ktor", "ktor-server-tests", ktorVersion) {
        exclude("junit")
    }
    testImplementation("org.junit.jupiter", "junit-jupiter", jupiterVersion)
    testImplementation("io.mockk", "mockk", mockkVersion)
    testImplementation("org.testcontainers", "junit-jupiter", testContainersVersion)
    testImplementation("org.testcontainers", "postgresql", testContainersVersion)
    testImplementation("org.testcontainers", "testcontainers", testContainersVersion) {
        exclude("junit")
    }
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "14"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
    shadowJar {
        archiveVersion.set("")
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
