import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
        jcenter()
        maven { url = uri("http://files.minecraftforge.net/maven") }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath("net.minecraftforge.gradle:ForgeGradle:2.1-SNAPSHOT") {
            exclude(group = "net.sf.trove4j", module = "trove4j")
        }
    }
}

apply(plugin = "net.minecraftforge.gradle.forge")
apply(plugin = "kotlin")

plugins {
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.serialization") version "1.3.50"
    java
    idea
}

idea {
    module {
        inheritOutputDirs = true
    }
}

version = modVersion
group = "net.unaussprechlich.warlordsplus"

configure<ForgeExtension> {
    version = "1.8.9-11.15.1.2318-1.8.9"
    runDir = "run"
    mappings = "stable_22"
    makeObfSourceJar = false

    replace("@VERSION@", modVersion)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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
}

dependencies {
    embed(kotlin("stdlib-jdk8"))
    embed(kotlin("reflect"))
    embed("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    embed(ktor("client-cio"))

    embed(ktor("client-serialization-jvm")) {
        exclude(group = "org.jetbrains.kotlin")
    }

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))


}

tasks.create<Delete>("kotlinCleanHotfix") {
    delete = setOf(
        "WarlordsPlus.kotlin_module"
    )
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.includeRuntime = true
    }

    withType<ProcessResources> {
        copy {
            from("src/main/resources")
            into("build/classes/java/main")
        }
    }

    withType<Jar> {

        manifest {
            attributes("ModSide" to "CLIENT")
        }
        from(embed.map { if (it.isDirectory) it else zipTree(it) })

        setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)
    }
}