import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.asm.gradle.plugins.MixinExtension

val kotlinVersion = "1.3.50"
val ktorVersion = "1.2.5"
val coroutinesVersion = "1.3.2"

fun ktor(module: String) = "io.ktor:ktor-$module:$ktorVersion"
fun ktor() = "io.ktor:ktor:$ktorVersion"

val sourceCompatibility = JavaVersion.VERSION_1_8
val targetCompatibility = JavaVersion.VERSION_1_8

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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath("org.spongepowered:mixingradle:0.6-SNAPSHOT")
        classpath("net.minecraftforge.gradle:ForgeGradle:2.1-SNAPSHOT") {
            exclude(group = "net.sf.trove4j", module = "trove4j")
            exclude(group = "trove", module = "trove")
        }
    }
}

apply(plugin = "net.minecraftforge.gradle.forge")
apply(plugin = "kotlin")
apply(plugin = "org.spongepowered.mixin")

plugins {
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.serialization") version "1.3.50"
    java
    idea
}

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
    add(mainSourceSet, "main.refmap.json")
    defaultObfuscationEnv = "notch"
}

version = modVersion
group = "net.unaussprechlich.warlordsplus"

configure<ForgeExtension> {
    version = "1.8.9-11.15.1.2318-1.8.9"
    runDir = "run"
    mappings = "stable_22"
    makeObfSourceJar = false

    replace("@VERSION@", modVersion)

    coreMod = "net.unaussprechlich.mixin.CoreMod"

    val args = listOf(
        "-Dfml.coreMods.load=net.unaussprechlich.mixin.CoreMod",
        "-Dmixin.env.compatLevel=JAVA_8", //needed to use java 8 when using mixins
        "-Dmixin.debug.verbose=true", //verbose mixin output for easier debugging of mixins
        "-Dmixin.debug.export=true", //export classes from mixin to runDirectory/.mixin.out
        "-XX:-OmitStackTraceInFastThrow", //without this sometimes you end up with exception with empty stacktrace
        "-XX:-DisableExplicitGC"  // fast world loading
    )

    clientJvmArgs.addAll(args)
    serverJvmArgs.addAll(args)
}

val embed by configurations.creating
configurations.compile.extendsFrom(embed)

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
}

dependencies {
    embed(kotlin("stdlib-jdk8"))
    embed(kotlin("reflect"))
    embed("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    embed(ktor("client-cio"))

    embed(ktor("client-serialization-jvm")) {
        exclude(group = "org.jetbrains.kotlin")
    }

    embed("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        exclude(mapOf("module" to "commons-io"))
        exclude(mapOf("module" to "guava"))
        exclude(mapOf("module" to "gson"))
    }

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))

}
/*
tasks.create<Delete>("kotlinCleanHotfix") {
    delete = setOf(
        "WarlordsPlus.kotlin_module"
    )
}*/

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.includeRuntime = true
    }

    withType<ProcessResources> {
        inputs.property("version", project.version)
        inputs.property("mcversion", minecraft.version)

        // replace stuff in mcmod.info, nothing else
        from(sourceSets.getByName("main").resources.srcDirs) {
            include("mcmod.info")

            // replace version and mcversion
            expand(mapOf("version" to project.version, "mcversion" to minecraft.version))
        }

        // copy everything else, thats not the mcmod.info
        from(sourceSets.getByName("main").resources.srcDirs) {
            exclude("mcmod.info")
        }
    }

    withType<Jar> {

        manifest {
            attributes["FMLCorePlugin"] = "net.unaussprechlich.mixin.CoreMod"
            attributes["FMLCorePluginContainsMod"] = "true"
            attributes["ModSide"] = "CLIENT"
            attributes["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
            attributes["MixinConfigs"] = "mixin.config.json"
            attributes["TweakOrder"] = "0"
            attributes["ForceLoadAsMod"] = "true"
        }
        from(embed.map { if (it.isDirectory) it else zipTree(it) })

        setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)
    }
}

