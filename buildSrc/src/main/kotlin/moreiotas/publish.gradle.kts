package moreiotas

import gradle.kotlin.dsl.accessors._63fbed089a48cacc8b61113b9deae027.publishMods
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.provideDelegate

plugins {
    id("me.modmuss50.mod-publish-plugin")
    id("moreiotas.utils.mod-dependencies")
}

val modVersion: String by project

publishMods {
    val isCI = (System.getenv("CI") ?: "").isNotBlank()
    val isDryRun = (System.getenv("DRY_RUN") ?: "").isNotBlank()
    dryRun = !isCI || isDryRun

    type = STABLE
    changelog = provider { getLatestChangelog() }

    github {
        accessToken = System.getenv("GITHUB_TOKEN") ?: ""
    }
}

val sectionHeaderPrefix = "## "

fun getLatestChangelog() = rootProject.file("CHANGELOG.md").useLines { lines ->
    lines.dropWhile { !it.startsWith(sectionHeaderPrefix) }
        .withIndex()
        .takeWhile { it.index == 0 || !it.value.startsWith(sectionHeaderPrefix) }
        .joinToString("\n") { it.value }
        .trim()
}

fun String.capitalize() = replaceFirstChar(Char::uppercase)
