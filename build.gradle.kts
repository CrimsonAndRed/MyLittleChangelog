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
    val kotlinVersion = "1.4.0"
    application
    java
//    jacoco
    kotlin("jvm") version kotlinVersion
}

val ktorVersion = "1.3.2"
val logbackVersion = "1.2.3"

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "14"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
//    jacocoTestCoverageVerification {
//        violationRules {
//            rule {
//                limit {
//                    minimum = "0.75".toBigDecimal()
//                }
//            }
//        }
//    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events ("passed", "skipped", "failed")
    }
}