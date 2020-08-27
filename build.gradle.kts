/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import java.util.*

plugins {
    kotlin("multiplatform") version "1.4.0"
    kotlin("plugin.serialization") version "1.4.0"
    id("org.jetbrains.dokka") version "1.4.0-rc"
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = "2020.8.28-SNAPSHOT"

// common
val ktorVersion = "1.4.0"
val coroutinesVersion = "1.3.9"
val serializationVersion = "1.0.0-RC"
val cliktVersion = "2.8.0"

// jvm
val exposedVersion = "0.26.2"
val postgresqlVersion = "42.2.14"
val hikariVersion = "3.4.5"
val kamlVersion = "0.19.0"

repositories {
    jcenter()
    mavenCentral()
}

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    js {
        nodejs {
            testTask {
                enabled = false // complains about missing DOM, TODO fix that, maybe, I'm not sure that I want the dependency
            }
        }
    }

    sourceSets {

        all {
            languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
            languageSettings.useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
            languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.useExperimentalAnnotation("io.ktor.util.KtorExperimentalAPI")
        }

        commonMain {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                api("io.ktor:ktor-client-core:$ktorVersion")
                api("io.ktor:ktor-client-websockets:$ktorVersion")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                api("io.ktor:ktor-websockets:$ktorVersion")
                api("io.ktor:ktor-client-cio:$ktorVersion") // TODO check if we want this one (CIO) or another
                api("io.ktor:ktor-auth:$ktorVersion")
                api("io.ktor:ktor-serialization:$ktorVersion")
                api("ch.qos.logback:logback-classic:1.2.3")
                api("org.jetbrains.exposed:exposed-core:$exposedVersion")
                api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
                api("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
                api("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
                implementation("org.postgresql:postgresql:$postgresqlVersion")
                implementation("com.zaxxer:HikariCP:$hikariVersion")
                api("com.github.ajalt:clikt-multiplatform:$cliktVersion")
                api("com.charleskorn.kaml:kaml:$kamlVersion")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jsMain by getting {
            dependencies {
                api("io.ktor:ktor-client-js:$ktorVersion")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

tasks.named("compileKotlinJs") {
    this as org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
    kotlinOptions.moduleKind = "umd"
}


tasks.withType<Jar> {
    manifest {
        attributes += sortedMapOf(
            "Built-By" to System.getProperty("user.name"),
            "Build-Jdk" to System.getProperty("java.version"),
            "Implementation-Vendor" to "Simplexion Kft.",
            "Implementation-Version" to archiveVersion.get(),
            "Created-By" to org.gradle.util.GradleVersion.current()
        )
    }
}

// Dokka Crashes
//tasks.dokkaHtml {
//    outputDirectory = "$buildDir/dokka"
//    dokkaSourceSets {
//        val commonMain by creating {}
//        val jvmMain by creating {}
//        val jsMain by creating {}
//    }
//}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

publishing {

    val properties = Properties()
    val propFile = File(project.findProperty("gpr.properties").toString())
    properties.load(propFile.inputStream())

    repositories {
        if (project.version.toString().contains("SNAPSHOT")) {
            mavenLocal()
        } else {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/spxbhuhb/zakadabar-stack")
                credentials {
                    username = properties["user"].toString()
                    password = properties["key"].toString()
                }
            }
        }
    }

    publications.withType<MavenPublication>().all {

        pom {
            description.set("Kotlin/Ktor based web server and client")
            name.set("Zakadabar")
            url.set("https://github.com/spxbhuhb/zakadabar-stack")
            scm {
                url.set("https://github.com/spxbhuhb/zakadabar-stack")
                connection.set("scm:git:git://github.com/spxbhuhb/zakadabar-stack.git")
                developerConnection.set("scm:git:ssh://git@github.com/spxbhuhb/zakadabar-stack.git")
            }
            licenses {
                license {
                    name.set("AGPL 3.0")
                    url.set("https://www.gnu.org/licenses/agpl-3.0.txt")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("toth-istvan-zoltan")
                    name.set("Tóth István Zoltán")
                    url.set("https://github.com/toth-istvan-zoltan")
                    organization.set("Simplexion Kft.")
                    organizationUrl.set("https://www.simplexion.hu")
                }
            }
        }
    }
}