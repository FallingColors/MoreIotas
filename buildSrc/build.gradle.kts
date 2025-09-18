plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // plugins used in convention plugins
    implementation("me.modmuss50.mod-publish-plugin:me.modmuss50.mod-publish-plugin.gradle.plugin:1.0.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
