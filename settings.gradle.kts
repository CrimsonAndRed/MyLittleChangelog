pluginManagement {
    val kotlinVersion: String by settings
    val shadowVersion: String by settings
    val ktlintVersion: String by settings
    val versionsVersion: String by settings
    plugins {
        application
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("com.github.johnrengelman.shadow") version shadowVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
        id("com.github.ben-manes.versions") version versionsVersion
    }
}

rootProject.name = "MyLittleChangelog"
