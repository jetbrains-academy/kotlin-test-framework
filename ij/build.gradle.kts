group = rootProject.group
version = rootProject.version

plugins {
    id("org.jetbrains.intellij") version "1.14.1"
}

fun properties(key: String) = providers.gradleProperty(key)

intellij {
    pluginName = properties("pluginName")
    version = properties("platformVersion")
    type = properties("platformType")

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins = properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) }
}
