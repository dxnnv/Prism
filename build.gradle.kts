plugins {
    kotlin("jvm") version "2.0.20"
    id("com.gradleup.shadow") version "8.3.5"
}

group = "dev.dxnny"
version = "2.0.0"
val jarName = "${project.name}-${version}.jar"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
        name = "placeholderapi"
    }
    maven("https://repo.xenondevs.xyz/releases") {
        name = "xenondevsReleases"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")

    implementation("dev.dxnny:infrastructure:2.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("xyz.xenondevs.invui:invui:1.46")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.46")
}

val targetJavaVersion = 21
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    delete("$projectDir/jars/$jarName")
    archiveFileName.set(jarName)

    relocate("org.bstats", "${group}.${project.name.lowercase()}.libs.bstats")

    exclude("org/intellij/lang/annotations/**")
    exclude("org/jetbrains/annotations/**")

    destinationDirectory.set(file("jars"))
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
