import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val sourceCompatibility = JavaVersion.VERSION_1_8
val targetCompatibility = JavaVersion.VERSION_1_8

var modVersion = "DEV_${Math.abs(System.currentTimeMillis().hashCode())}"

//Getting the Version if we Build on Travis
if(System.getenv()["RELEASE_VERSION"]  != null ){
    modVersion = "${System.getenv()["RELEASE_VERSION"]}"
}

buildscript {

    repositories {
        jcenter()
        maven { url = uri("http://files.minecraftforge.net/maven") }
        maven { url = uri("https://plugins.gradle.org/m2") }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
        classpath("net.minecraftforge.gradle:ForgeGradle:2.1-SNAPSHOT") {
            exclude(group = "net.sf.trove4j", module = "trove4j")
        }
    }
}

apply(plugin = "kotlin")
apply(plugin = "net.minecraftforge.gradle.forge")

plugins {
    java
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
    idea
}

idea {
    module {
        inheritOutputDirs = true
    }
}


version = modVersion
group = "net.unaussprechlich"


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
    maven(url = "https://dl.bintray.com/kotlin/ktor")
    maven(url = "https://dl.bintray.com/kotlin/kotlinx")
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.72"))
    implementation(kotlin("stdlib-jdk7", "1.3.72"))
    implementation(kotlin("reflect", "1.3.72"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.7")

    implementation("io.ktor:ktor-client-cio:1.3.2")
    implementation("io.ktor:ktor-client-serialization-jvm:1.3.2")
    implementation("io.ktor:ktor-client-serialization:1.3.2")

    testImplementation("org.jetbrains.kotlin:kotlin-test:1.3.72")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.72")
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