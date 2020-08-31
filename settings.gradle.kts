pluginManagement {
    val kotlinVersion: String by settings
    val ktorVersion: String by settings
    val logbackVersion: String by settings
    val shadowVersion: String by settings
    plugins {
        application
        kotlin("jvm") version kotlinVersion
        id("com.github.johnrengelman.shadow") version shadowVersion
    }
}

rootProject.name = "MyLittleChangelog"
