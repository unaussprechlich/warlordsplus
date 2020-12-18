import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.user.ReobfMappingType
import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.gradle.api.file.DuplicatesStrategy.EXCLUDE
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.asm.gradle.plugins.MixinExtension

val MINECRAFT_VERSION = "1.8.9-11.15.1.2318-1.8.9"
val MOD_NAME = "warlordsplus"
val MAIN_CLASS = "WarlordsPlus.java"

val kotlinVersion = "1.3.50"
val ktorVersion = "1.2.5"
val coroutinesVersion = "1.3.2"

var modVersion = "DEV_${Math.abs(System.currentTimeMillis().hashCode())}"

//Getting the Version if we Build on Travis
if (System.getenv()["RELEASE_VERSION"] != null) {
    modVersion = "${System.getenv()["RELEASE_VERSION"]}"
}

buildscript {

    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("http://files.minecraftforge.net/maven") }
        maven {
            setUrl("http://repo.spongepowered.org/maven")
        }
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("org.spongepowered:mixingradle:0.6-SNAPSHOT")
        classpath("com.github.jengelman.gradle.plugins:shadow:1.2.3")
        classpath("net.minecraftforge.gradle:ForgeGradle:2.1-SNAPSHOT") {
            exclude(group = "net.sf.trove4j", module = "trove4j")
            exclude(group = "trove", module = "trove")
        }
    }
}

apply(plugin = "com.github.johnrengelman.shadow")
apply(plugin = "net.minecraftforge.gradle.forge")
apply(plugin = "org.spongepowered.mixin")

plugins {
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.serialization") version "1.3.50"
    java
    idea
}

fun ktor(module: String) = "io.ktor:ktor-$module:$ktorVersion"
fun ktor() = "io.ktor:ktor:$ktorVersion"

val sourceCompatibility = JavaVersion.VERSION_1_8
val targetCompatibility = JavaVersion.VERSION_1_8

val sourceSets = the<JavaPluginConvention>().sourceSets
val mainSourceSet = sourceSets.getByName("main")
val minecraft = the<ForgeExtension>()

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


configure<ForgeExtension> {
    version = MINECRAFT_VERSION
    runDir = "run"
    mappings = "stable_22"

    replace(mapOf("@VERSION@" to modVersion))
}

repositories {
    jcenter()
    mavenCentral()
    "http://dl.bintray.com/kotlin".let {
        maven { setUrl("$it/ktor") }
        maven { setUrl("$it/kotlinx") }
    }
    maven {
        setUrl("http://repo.spongepowered.org/maven")
    }
    maven {
        setUrl("https://plugins.gradle.org/m2/")
    }
}

val compile by configurations
val embed by configurations.creating
compile.extendsFrom(embed)

dependencies {
    embed(kotlin("stdlib-jdk8"))
    embed(kotlin("reflect"))
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

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))

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

fun configureShadowJar(task: ShadowJar, classifier: String) {
    task.configurations = listOf(embed)
    task.exclude("META-INF/MUMFREY*")
    task.from(sourceSets["main"].output)
    task.from(sourceSets["api"].output)
    task.from("$buildDir/tmp/compileJava/")
    task.classifier = classifier

    task.setDuplicatesStrategy(EXCLUDE)
    task.exclude("META-INF/maven/", "META-INF/nar/", "module-info.class", "META-INF/versions/")
}

shadowJar.apply { configureShadowJar(this, "") }

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
        kotlinOptions.includeRuntime = true
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
    }

    withType<Jar> {
        exclude("LICENSE.txt", "log4j2.xml")
        setDuplicatesStrategy(EXCLUDE)
        configureManifest(manifest)
    }
}


artifacts {
    add("archives", shadowJar)
    add("archives", jar)
}