/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.Versions
import zakadabar.gradle.config
import zakadabar.gradle.isPublishing
import zakadabar.gradle.manifestAndDokka

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.dokka")
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    js(BOTH) {
        nodejs {
            testTask {
                enabled = false // complains about missing DOM, TODO fix that, maybe, I'm not sure that I want the dependency
            }
        }
    }

    sourceSets {

        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlin.ExperimentalStdlibApi")
            languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
            languageSettings.optIn("kotlin.ExperimentalUnsignedTypes")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("io.ktor.util.KtorExperimentalAPI")
        }

        commonMain {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")
                api("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.datetime}")
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
                api("io.ktor:ktor-server-core:${Versions.ktor}")
                implementation("io.ktor:ktor-server-netty:${Versions.ktor}")
                implementation("io.ktor:ktor-server-sessions:${Versions.ktor}")
                api("io.ktor:ktor-websockets:${Versions.ktor}")
                api("io.ktor:ktor-auth:${Versions.ktor}")
                api("io.ktor:ktor-serialization:${Versions.ktor}")
                api("io.ktor:ktor-client-serialization:${Versions.ktor}")
                api("io.ktor:ktor-client-cio:${Versions.ktor}") // TODO check if we want this one (CIO) or another
                api("ch.qos.logback:logback-classic:${Versions.logback}")
                api("org.jetbrains.exposed:exposed-core:${Versions.exposed}")
                api("org.jetbrains.exposed:exposed-dao:${Versions.exposed}")
                api("org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}")
                api("org.jetbrains.exposed:exposed-java-time:${Versions.exposed}")
                implementation("org.postgresql:postgresql:${Versions.postgresql}")
                implementation("com.zaxxer:HikariCP:${Versions.hikari}")
                api("com.github.ajalt.clikt:clikt:${Versions.clikt}")
                api("com.charleskorn.kaml:kaml:${Versions.kaml}")
            }
        }

        sourceSets["jvmTest"].dependencies {
            implementation("com.h2database:h2:${Versions.h2}")
        }

        @Suppress("UNUSED_VARIABLE")
        val jsMain by getting {

        }

        @Suppress("UNUSED_VARIABLE")
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

//tasks.named("compileKotlinJs") {
//    this as org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
//    kotlinOptions.moduleKind = "umd"
//}

noArg {
    annotation("kotlinx.serialization.Serializable")
}

if (project.isPublishing) {

    manifestAndDokka(tasks)

    signing { config(publishing.publications) }

    publishing {
        config(project)

        publications.withType<MavenPublication>().all {
            config(tasks["javadocJar"], "Zakadabar Core")
        }
    }

}
