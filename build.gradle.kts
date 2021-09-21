import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.user.ReobfMappingType
import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.gradle.api.file.DuplicatesStrategy.EXCLUDE
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.asm.gradle.plugins.MixinExtension

val MINECRAFT_VERSION : String by project
val MOD_NAME : String by project
val MAIN_CLASS : String by project

val kotlinVersion: String by project
val ktorVersion: String by project
val coroutinesVersion: String by project

var modVersion = "DEV_${Math.abs(System.currentTimeMillis().hashCode())}"

//Getting the Version if we Build on Travis
if (System.getenv()["RELEASE_VERSION"] != null) {
    modVersion = "${System.getenv()["RELEASE_VERSION"]}"
}

buildscript {

    repositories {
        mavenCentral()
        jcenter()
        maven {
            setUrl("https://maven.minecraftforge.net")
        }
        maven{
            setUrl("https://jitpack.io/")
        }
        maven {
            setUrl("https://repo.spongepowered.org/maven")
        }
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.github.xcfrg:MixinGradle:0.6-SNAPSHOT")
        classpath("com.github.asbyth:ForgeGradle:6f53277") {
            exclude(group = "net.sf.trove4j", module = "trove4j")
            exclude(group = "trove", module = "trove")
        }
    }
}

apply(plugin = "net.minecraftforge.gradle.forge")
apply(plugin = "org.spongepowered.mixin")

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    java
    idea
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

fun ktor(module: String) = "io.ktor:ktor-$module:$ktorVersion"
fun ktor() = "io.ktor:ktor:$ktorVersion"

val sourceCompatibility = JavaVersion.VERSION_1_8
val targetCompatibility = JavaVersion.VERSION_1_8

val sourceSets = the<JavaPluginConvention>().sourceSets
val mainSourceSet = sourceSets.getByName("main")
val minecraft = the<ForgeExtension>()

configure<ForgeExtension> {
    version = MINECRAFT_VERSION
    runDir = "run"
    mappings = "stable_22"

    coreMod = "net.unaussprechlich.mixin.CoreMod"

    clientJvmArgs.add("-Dfml.coreMods.load=$coreMod")

    clientRunArgs.add("--username=${System.getenv()["EMAIL"]}")
    clientRunArgs.add("--password=${System.getenv()["PASSWORD"]}")

    replace(mapOf("@VERSION@" to modVersion))
    replaceIn(MAIN_CLASS)
}


configure<IdeaModel> {
    module.apply {
        inheritOutputDirs = true
    }
    module.isDownloadJavadoc = true
    module.isDownloadSources = true
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

configure<MixinExtension> {
    add(mainSourceSet, "net.unaussprechlich.${MOD_NAME}.refmap.json")
}

version = modVersion
group = "net.unaussprechlich.${MOD_NAME}"


repositories {
    jcenter()
    mavenCentral()
    maven {
        setUrl("https://repo.spongepowered.org/maven")
    }
    maven {
        setUrl("https://plugins.gradle.org/m2/")
    }
}

val compile by configurations
val embed by configurations.creating
compile.extendsFrom(embed)

dependencies {
    embed(kotlin("stdlib-jdk8", kotlinVersion))
    embed(kotlin("reflect", kotlinVersion))
    embed("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    embed(ktor("client-cio"))

    embed(ktor("client-serialization-jvm")) {
        exclude(group = "org.jetbrains.kotlin")
    }

    embed("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        exclude(mapOf("module" to "launchwrapper"))
        exclude(mapOf("module" to "guava"))
        exclude(mapOf("module" to "gson"))
        isTransitive = false
    }

    testImplementation(kotlin("test", kotlinVersion))
    testImplementation(kotlin("test-junit", kotlinVersion))

    embed("com.jagrosh:DiscordIPC:0.4")
}

val shadowJar: ShadowJar by tasks
val build by tasks
val jar by tasks

fun configureManifest(manifest: Manifest) {
    manifest.attributes["FMLCorePlugin"] = "net.unaussprechlich.${MOD_NAME}.mixin.CoreMod"
    manifest.attributes["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
    manifest.attributes["MixinConfigs"] = "mixin.${MOD_NAME}.json"
    manifest.attributes["TweakOrder"] = "0"
    manifest.attributes["ForceLoadAsMod"] = true
    manifest.attributes["FMLCorePluginContainsFMLMod"] = true
    manifest.attributes["ModSide"] = "CLIENT"
}

fun configureShadowJar(task: ShadowJar) {
    task.configurations = listOf(embed)
    task.exclude("META-INF/MUMFREY*")
    task.from(sourceSets["main"].output)
    task.from(sourceSets["api"].output)
    task.from("$buildDir/tmp/compileJava/")

    task.duplicatesStrategy = EXCLUDE
    task.exclude("META-INF/maven/", "META-INF/nar/", "module-info.class", "META-INF/versions/")
}

shadowJar.apply { configureShadowJar(this) }

configure<NamedDomainObjectContainer<net.minecraftforge.gradle.user.IReobfuscator>> {
    create("shadowJar").apply {
        mappingType = ReobfMappingType.SEARGE
    }
}

tasks.getByName("reobfJar").apply {
    dependsOn("shadowJar")
}

tasks.getByName("sourceJar").enabled = false

tasks {

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<ProcessResources> {
        //val tokens = mapOf("@VERSION@" to version)

        inputs.property("version", version)
        inputs.property("mcversion", minecraft.version)

        // replace stuff in mcmod.info, nothing else
        from(mainSourceSet.resources.srcDirs) {
            include("mcmod.info")

            // replace version and mcversion
            expand(mapOf("version" to version, "mcversion" to minecraft.version))
        }

        // copy everything else, thats not the mcmod.info
        from(mainSourceSet.resources.srcDirs) {
            exclude("mcmod.info")
        }

        //Copy resources if we are in local dev. ForgeGradle seems broken with modern IntelliJ
        if (System.getenv()["RELEASE_VERSION"] == null) {
            copy {
                from("$projectDir/src/main/resources")
                into("$projectDir/build/classes/java/main")
            }
        }
    }

    withType<Jar> {
        exclude("LICENSE.txt", "log4j2.xml")
        duplicatesStrategy = EXCLUDE
        configureManifest(manifest)
    }
}


artifacts {
    add("archives", shadowJar)
    add("archives", jar)
}