package moreiotas.utils

plugins {
    id("me.modmuss50.mod-publish-plugin")
}

abstract class MoreIotasModDependenciesExtension(private val project: Project) {
    fun requires(slug: String) = requires(slug, slug)

    fun requires(slugs: Map<String, String>) = requires(slugs["curseforge"]!!, slugs["modrinth"]!!)

    fun requires(curseforge: String, modrinth: String) = project.run {
        publishMods {
            curseforge { requires(curseforge) }
            modrinth { requires(modrinth) }
        }
    }

    fun optional(slug: String) = optional(slug, slug)

    fun optional(slugs: Map<String, String>) = optional(slugs["curseforge"]!!, slugs["modrinth"]!!)

    fun optional(curseforge: String, modrinth: String) = project.run {
        publishMods {
            curseforge { optional(curseforge) }
            modrinth { optional(modrinth) }
        }
    }
}

val extension = extensions.create<MoreIotasModDependenciesExtension>("moreiotasModDependencies")
