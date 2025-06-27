plugins {
    idea
    id("fabric-loom")
    id ("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
}

apply(from = "${rootProject.projectDir}/build.common.gradle.kts")

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
base.archivesName = property("mod.id") as String

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric-api")}")
    modImplementation("maven.modrinth:midnightlib:${property("deps.midnightlib")}")
    include("maven.modrinth:midnightlib:${property("deps.midnightlib")}")

    implementation("org.jetbrains:annotations:${property("deps.annotations")}")
}

jsonlang {
    languageDirectories = listOf("assets/bribery/lang")
    prettyPrint = true
}

tasks {
    processResources {
        exclude("**/neoforge.mods.toml", "**/mods.toml")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=1.21")) {
        JavaVersion.VERSION_21
    } else {
        JavaVersion.VERSION_17
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

val additionalVersionsStr = findProperty("publish.additionalVersions") as String?
val additionalVersions: List<String> = additionalVersionsStr
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = tasks.remapJar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.remapSourcesJar.map { it.archiveFile.get() })

    val sinytra = findProperty("deps.sinytra")?.toString()?.toBoolean() ?: false

    type = BETA
    displayName = "Bribery ${property("mod.version")} for ${stonecutter.current.version} Fabric"
    version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
    changelog = provider {
        val changelog = rootProject.file("CHANGELOG.md").readText()
        if (sinytra) {
            "## Sinytra and Forgified Fabric API are required on Forge!!!\n\n${changelog}"
        } else {
            changelog
        }
    }
    modLoaders.add("fabric")
    if (sinytra) {
        modLoaders.add("forge")
    }

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = providers.environmentVariable("MODRINTH_API_KEY")
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        embeds("midnightlib")
        if (sinytra) {
            optional("connector", "forgified-fabric-api")
        }
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = providers.environmentVariable("CURSEFORGE_API_KEY")
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        embeds("midnightlib")
        if (sinytra) {
            optional("sinytra-connector", "forgified-fabric-api")
        }
    }
}
