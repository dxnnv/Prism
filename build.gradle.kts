import java.nio.file.Files

plugins {
    kotlin("jvm") version "2.0.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.dxnny"
version = "1.0.0"

repositories {
    mavenCentral()
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
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.36")
    implementation("xyz.xenondevs.invui:invui-core:1.36")
    implementation("xyz.xenondevs.invui:inventory-access-r16:1.36")
    implementation("xyz.xenondevs.invui:inventory-access-r17:1.36")
    implementation("xyz.xenondevs.invui:inventory-access-r18:1.36")
    implementation("xyz.xenondevs.invui:inventory-access-r19:1.36")
    implementation("xyz.xenondevs.invui:inventory-access-r20:1.33")
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
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

tasks.register<Delete>("cleanDest") {
    delete("C:/Users/Danny/IdeaProjects/Prism/jars/Prism-${version}.jar")
}

tasks.shadowJar {
    dependsOn("cleanDest")
    archiveFileName.set("Prism-${version}.jar")

    exclude("org/intellij/lang/annotations/**")
    exclude("org/jetbrains/annotations/**")

    destinationDirectory.set(file("jars"))
}

tasks.register("moveJar") {
    dependsOn(tasks.shadowJar)
    doLast {
        val sourceFile = tasks.shadowJar.get().archiveFile.get().asFile
        val destinationFile = file("C:/Users/Danny/IdeaProjects/Prism/jars/Prism-${version}.jar")
        Files.copy(sourceFile.toPath(), destinationFile.toPath())
    }
}

artifacts {
    archives(tasks.shadowJar)
}

tasks.build {
    dependsOn(tasks.shadowJar)
    finalizedBy("moveJar")
}
