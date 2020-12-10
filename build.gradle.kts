import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.user.ReobfMappingType
import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.asm.gradle.plugins.MixinExtension

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


val kotlinVersion = "1.3.50"
val ktorVersion = "1.2.5"
val coroutinesVersion = "1.3.2"

fun ktor(module: String) = "io.ktor:ktor-$module:$ktorVersion"
fun ktor() = "io.ktor:ktor:$ktorVersion"

val sourceCompatibility = JavaVersion.VERSION_1_8
val targetCompatibility = JavaVersion.VERSION_1_8

group = "net.unaussprechlich.warlordsplus"
version = "DEV_${Math.abs(System.currentTimeMillis().hashCode())}"

val sourceSets = the<JavaPluginConvention>().sourceSets
val mainSourceSet = sourceSets.getByName("main")
val minecraft = the<ForgeExtension>()

//Getting the Version if we Build on Travis
if (System.getenv()["RELEASE_VERSION"] != null) {
    version = "${System.getenv()["RELEASE_VERSION"]}"
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
    add(mainSourceSet, "net.unaussprechlich.warlordsplus.refmap.json")
}

configure<ForgeExtension> {
    version = "1.8.9-11.15.1.2318-1.8.9"
    runDir = "run"
    mappings = "stable_22"

    makeObfSourceJar = true

    coreMod = "net.unaussprechlich.mixin.CoreMod"

    replace("@VERSION@", version)
    /*

    val args = listOf(
        "-Dmixin.env.compatLevel=JAVA_8", //needed to use java 8 when using mixins
        "-Dmixin.debug.verbose=true", //verbose mixin output for easier debugging of mixins
        "-Dmixin.debug.export=true", //export classes from mixin to runDirectory/.mixin.out
        "-XX:-OmitStackTraceInFastThrow", //without this sometimes you end up with exception with empty stacktrace
        "-XX:-DisableExplicitGC"  // fast world loading
    )

    clientJvmArgs.addAll(args)
    serverJvmArgs.addAll(args)*/
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

val sourceJar: Jar by tasks
val shadowJar: ShadowJar by tasks
val build by tasks
val jar by tasks
val reobfJar by tasks


fun configureManifest(manifest: Manifest) {
    manifest.attributes["FMLCorePlugin"] = "net.unaussprechlich.mixin.CoreMod"
    manifest.attributes["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
    manifest.attributes["MixinConfigs"] = "mixin.config.json"
    manifest.attributes["TweakOrder"] = "0"
    manifest.attributes["ForceLoadAsMod"] = "true"
}


fun configureShadowJar(task: ShadowJar, classifier: String) {
    task.configurations = listOf(embed)
    task.exclude("META-INF/MUMFREY*")
    task.from(sourceSets["main"].output)
    task.from(sourceSets["api"].output)
    task.from("$buildDir/tmp/compileJava/")
    task.classifier = classifier
}

shadowJar.apply { configureShadowJar(this, "") }

configure<NamedDomainObjectContainer<net.minecraftforge.gradle.user.IReobfuscator>> {
    create("shadowJar").apply {
        mappingType = ReobfMappingType.SEARGE
    }
}
tasks.getByName("build").apply {
    dependsOn("reobfShadowJar")
}

build.dependsOn(shadowJar)


tasks {

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.includeRuntime = true
    }

    withType<ProcessResources> {
        inputs.property("version", project.version)
        inputs.property("mcversion", minecraft.version)

        // replace stuff in mcmod.info, nothing else
        from(mainSourceSet.resources.srcDirs) {
            include("mcmod.info")

            // replace version and mcversion
            expand(mapOf("version" to project.version, "mcversion" to minecraft.version))
        }

        // copy everything else, thats not the mcmod.info
        from(mainSourceSet.resources.srcDirs) {
            exclude("mcmod.info")
        }
    }

    withType<Jar> {
        //from("$buildDir/tmp/compileJava/")
        from(sourceSets["main"].output)
        from(sourceSets["api"].output)

        exclude("LICENSE.txt", "log4j2.xml")

        setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)

        configureManifest(manifest)
    }
}


artifacts {
    add("archives", shadowJar)
    add("archives", jar)
}