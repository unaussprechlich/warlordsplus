import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.gradle.api.file.DuplicatesStrategy.EXCLUDE
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
            setUrl("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.github.asbyth:ForgeGradle:6f53277") {
            exclude(group = "net.sf.trove4j", module = "trove4j")
            exclude(group = "trove", module = "trove")
        }
    }
}

apply(plugin = "net.minecraftforge.gradle.forge")

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    java
    idea
}

fun ktor(module: String) = "io.ktor:ktor-$module:$ktorVersion"
fun ktor() = "io.ktor:ktor:$ktorVersion"

val sourceSets = the<JavaPluginConvention>().sourceSets
val mainSourceSet = sourceSets.getByName("main")
val minecraft = the<ForgeExtension>()

configure<ForgeExtension> {
    version = MINECRAFT_VERSION
    runDir = "run"
    mappings = "stable_22"

//    clientRunArgs.add("--username=${System.getenv()["EMAIL"]}")
//    clientRunArgs.add("--password=${System.getenv()["PASSWORD"]}")
    clientRunArgs.add("-Ddevauth.enabled=true}")

    makeObfSourceJar = false

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

    testImplementation(kotlin("test", kotlinVersion))
    testImplementation(kotlin("test-junit", kotlinVersion))

    //embed("com.jagrosh:DiscordIPC:0.4")
    //implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.1")
}

tasks {

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<ProcessResources> {

        inputs.property("version", version)
        inputs.property("mcversion", minecraft.version)

        from(mainSourceSet.resources.srcDirs) {
            include("mcmod.info")

            expand(mapOf("version" to version, "mcversion" to minecraft.version))
        }

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
        duplicatesStrategy = EXCLUDE

        from(embed.map { if (it.isDirectory) it else zipTree(it) })

        manifest.apply {
            attributes["ModSide"] = "CLIENT"
        }
    }
}
