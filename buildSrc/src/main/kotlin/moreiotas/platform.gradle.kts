package moreiotas

plugins {
    id("me.modmuss50.mod-publish-plugin")
    id("moreiotas.publish")
    id("moreiotas.utils.mod-dependencies")
}

val platform: String by project
val curseforgeId: String by project
val modrinthId: String by project
val modVersion: String by project
val minecraftVersion: String by project

publishMods {
    displayName = "$modVersion-$platform"
    modLoaders.add(platform)

    curseforge {
        accessToken = System.getenv("CURSEFORGE_TOKEN") ?: ""
        projectId = curseforgeId
        minecraftVersions.add(minecraftVersion)
        clientRequired = true
        serverRequired = true
    }

    modrinth {
        accessToken = System.getenv("MODRINTH_TOKEN") ?: ""
        projectId = modrinthId
        minecraftVersions.add(minecraftVersion)
    }

    github {
        parent(rootProject.tasks.named("publishGithub"))
    }
}
