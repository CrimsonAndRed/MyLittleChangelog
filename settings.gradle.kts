pluginManagement {
    val kotlinVersion: String by settings
    val ktorVersion: String by settings
    val logbackVersion: String by settings
    plugins {
        application
        kotlin("jvm") version kotlinVersion
    }
}

rootProject.name = "MyLittleChangelog"
