@file:Suppress("UnstableApiUsage")

import mythicaltemplate.Properties
import com.google.gson.GsonBuilder
import mythicaltemplate.MythicalMaven
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.function.Function
import javax.imageio.ImageIO
import javax.lang.model.element.Modifier


plugins {
    java
    idea
    id("fabric-loom") version ("1.4-SNAPSHOT")
    kotlin("jvm") version ("1.9.21")
    kotlin("kapt") version ("1.9.21")
    `maven-publish`
}
version = "1.0.0"
base.archivesName.set("${Properties.MOD_NAME}")

loom {
    mixin.defaultRefmapName.set("mixins.${Properties.MOD_ID}.refmap.json")
    interfaceInjection.enableDependencyInterfaceInjection.set(true)
    if(file("src/main/resources/${Properties.MOD_ID}.accesswidener").exists()){
        accessWidenerPath.set(file("src/main/resources/${Properties.MOD_ID}.accesswidener"))
    }
}

repositories {
    mavenCentral()
    maven("https://repo.lightdream.dev")
    maven("https://oss.sonatype.org/content/repositories/snapshots") {
        name = "sonatype-oss-snapshots1"
        mavenContent { snapshotsOnly() }
    }
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots1"
        mavenContent { snapshotsOnly() }
    }
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.wispforest.io")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.nucleoid.xyz/")
    maven("https://repo.lightdream.dev/")
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.blamejared.com")
    maven("https://jitpack.io")
    maven("https://maven.ladysnake.org/releases")
    maven("https://maven.parchmentmc.org")
    maven("https://maven2.bai.lol")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://maven.tterrag.com/")
    maven("https://repository.mythicalnetwork.live/repository/maven-releases/") {
        name = "Mythical-Repo"
        credentials {
            username = getMythicalCredentials().first
            password = getMythicalCredentials().second
        }
    }
}

val minecraftVersion = "1.20.1"

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    modImplementation("net.fabricmc:fabric-loader:0.14.25")
    modApi("net.fabricmc.fabric-api:fabric-api:0.91.0+$minecraftVersion")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$minecraftVersion:2023.09.03@zip")
    })

    include("io.wispforest:owo-sentinel:0.11.2+1.20")
    include("eu.pb4:placeholder-api:2.1.3+$minecraftVersion")?.let {
        modImplementation(it)
    }

    include("software.bernie.geckolib:geckolib-fabric-$minecraftVersion:4.2.4")?.let {
        modImplementation(it)
    }
    include("foundry.veil:Veil-fabric-$minecraftVersion:1.0.0.+")?.let {
        modImplementation(it)
    }
    include("dev.onyxstudios.cardinal-components-api:cardinal-components-base:5.2.2")?.let {
        modImplementation(it)
    }
    include("dev.onyxstudios.cardinal-components-api:cardinal-components-level:5.2.2")?.let {
        modImplementation(it)
    }
    include("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:5.2.2")?.let {
        modImplementation(it)
    }
    include("dev.onyxstudios.cardinal-components-api:cardinal-components-item:5.2.2")?.let {
        modImplementation(it)
    }

    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.16+kotlin.1.9.21")
    modImplementation("dev.architectury:architectury-fabric:9.1.12")
    modImplementation("net.luckperms:api:5.4")
    modImplementation("me.lucko:fabric-permissions-api:0.2-SNAPSHOT")
    modImplementation("com.terraformersmc:modmenu:7.2.2")
    modImplementation("com.mythicalnetwork:common-api:0.13.10")
    modImplementation("com.mythicalnetwork:fabric-api:0.13.10")
    modImplementation("dev.lightdream:redis-manager:3.0.9")
    modRuntimeOnly("lol.bai:badpackets:fabric-0.4.1")
    modImplementation("mcp.mobius.waila:wthit:fabric-8.4.3")
    modImplementation("ca.landonjw.gooeylibs:fabric:3.0.0-$minecraftVersion-SNAPSHOT@jar")
    modImplementation("io.wispforest:owo-lib:0.11.2+1.20")
    annotationProcessor("io.wispforest:owo-lib:0.11.2+1.20")
    kapt("io.wispforest:owo-lib:0.11.2+1.20")

    implementation("dev.lightdream:logger:4.2.0")
    implementation("dev.lightdream:file-manager:3.2.4")
    implementation("dev.lightdream:command-manager-common:6.1.4")
    modImplementation("dev.lightdream:command-manager-fabric-1-20:6.1.4")

    modCompileOnly("mcp.mobius.waila:wthit-api:fabric-8.4.3")

    modImplementation("com.cobblemon:fabric:MythicalCobblemon-1.4.1+1.20.1")
    modImplementation("com.mythicalnetwork:MythicalMod:1.9.0")
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = base.archivesName.get()
        from(components["java"])
    }

    repositories {
        mavenLocal()
        maven("https://repository.mythicalnetwork.live/repository/maven-releases/") {
            name = "Mythical-Repo"
            credentials {
                username = getMythicalCredentials().first
                password = getMythicalCredentials().second
            }
        }
    }
}

private fun getMythicalCredentials(): Pair<String?, String?> {
    var username = (project.findProperty("mythicalUsername") ?: System.getenv("MYTHICAL_USERNAME") ?: MythicalMaven.ID ?: "") as String?
    var password = (project.findProperty("mythicalPassword") ?: System.getenv("MYTHICAL_PASSWORD") ?: MythicalMaven.PASS ?: "") as String?
    return Pair(username, password)
}

tasks.create("hydrate") {
    doLast {
        val applyFileReplacements: Function<String, String> = Function { path ->
            path.replace("\$mod_name$", Properties.MOD_NAME)
                .replace("\$mod_id$", Properties.MOD_ID)
                .replace("\$mod_group$", Properties.MOD_GROUP)
        }
        val applyPathReplacements: Function<String, String> = Function { path ->
            path.replace("\$mod_name$", Properties.MOD_NAME)
                .replace("\$mod_id$", Properties.MOD_ID)
                .replace("\$mod_group$", Properties.MOD_GROUP.replace(".", "/"))
        }
        subprojects.forEach { subProject ->
            subProject.extensions.getByType<JavaPluginExtension>().sourceSets.forEach { sourceSet ->
                sourceSet.allSource.sourceDirectories.asFileTree.forEach { file ->
                    val newPath = Paths.get(applyPathReplacements.apply(file.path))
                    Files.createDirectories(newPath.parent)

                    if (!file.path.endsWith(".png")) {
                        val lines =
                            Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
                                .map { applyFileReplacements.apply(it) }
                        Files.deleteIfExists(file.toPath())
                        Files.write(
                            newPath,
                            lines
                        )
                    } else {
                        Files.move(file.toPath(), newPath)
                    }

                    var parent = file.parentFile
                    while (parent.listFiles()?.isEmpty() == true) {
                        Files.deleteIfExists(parent.toPath())
                        parent = parent.parentFile
                    }
                }
            }
        }
    }
}