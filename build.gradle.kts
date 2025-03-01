buildscript {
    @Suppress("UnstableApiUsage")
    configurations {
        classpath {
            resolutionStrategy {
                //in order to handle jackson's higher release version in shadow, this needs to be upgraded to latest.
                force("org.ow2.asm:asm:9.7")
                force("org.ow2.asm:asm-commons:9.7")
            }
        }
    }
}

plugins {
    kotlin("jvm") version "2.0.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.dxnny"
version = "0.0.5"

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
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation(files("$projectDir/libraries/Infrastructure-0.0.2.jar"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.44")
    implementation("xyz.xenondevs.invui:invui-core:1.44")
    implementation("xyz.xenondevs.invui:inventory-access-r16:1.44")
    implementation("xyz.xenondevs.invui:inventory-access-r17:1.44")
    implementation("xyz.xenondevs.invui:inventory-access-r18:1.44")
    implementation("xyz.xenondevs.invui:inventory-access-r19:1.44")
    implementation("xyz.xenondevs.invui:inventory-access-r20:1.44")
    implementation("xyz.xenondevs.invui:inventory-access-r21:1.44")
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
    delete("C:/Users/Danny/IdeaProjects/Prism/jars/Prism-${version}.jar")
    archiveFileName.set("Prism-${version}.jar")

    exclude("org/intellij/lang/annotations/**")
    exclude("org/jetbrains/annotations/**")

    destinationDirectory.set(file("jars"))
}

artifacts {
    archives(tasks.shadowJar)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
