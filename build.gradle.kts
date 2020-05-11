import org.gradle.jvm.tasks.Jar
import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by extra

val sourceCompatibility = JavaVersion.VERSION_1_8
val targetCompatibility = JavaVersion.VERSION_1_8

var modVersion = "DEV_${System.currentTimeMillis().hashCode()}"

//Getting the Version if we Build on Travis
if(System.getenv()["RELEASE_VERSION"]  != null ){
    modVersion = "${System.getenv()["RELEASE_VERSION"]}"
}

buildscript {
    repositories {
        jcenter()
        maven { url = uri("http://files.minecraftforge.net/maven") }
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:2.1-SNAPSHOT"){
            exclude(group = "net.sf.trove4j", module = "trove4j")
        }
    }
}

plugins{
    java
    kotlin("jvm") version "1.3.50"
}

apply(plugin = "net.minecraftforge.gradle.forge")

version = modVersion
group = "net.unaussprechlich"

configure<ForgeExtension> {
    version = "1.8.9-11.15.1.2318-1.8.9"
    runDir = "run"
    mappings = "stable_22"
    makeObfSourceJar = false

    replace("@VERSION@", modVersion)

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java{
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val embed by configurations.creating
configurations.compile.extendsFrom(embed)

repositories {
    jcenter()
    mavenCentral()
    maven (url = "http://maven.shadowfacts.net/")
}


dependencies {
    embed(kotlin("stdlib-jdk8"))
    embed("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.6")

    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

tasks.withType<Jar> {
    manifest {
        attributes("ModSide" to "CLIENT")
    }
    from(embed.map { if (it.isDirectory) it else zipTree(it) })
    setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)
}
