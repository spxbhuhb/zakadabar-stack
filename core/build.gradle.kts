/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.dependencies.Versions

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("com.android.library")
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

    js {
        nodejs {
            testTask {
                enabled = false // complains about missing DOM, TODO fix that, maybe, I'm not sure that I want the dependency
            }
        }
    }

    android {

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
        val androidMain by getting {
            kotlin.srcDir("src/commonJvmAndroid/kotlin")
            dependencies {
                api("io.ktor:ktor-server-core:${Versions.ktor}")
                implementation("io.ktor:ktor-server-netty:${Versions.ktor}")
                implementation("io.ktor:ktor-server-sessions:${Versions.ktor}")
                api("io.ktor:ktor-websockets:${Versions.ktor}")
                api("io.ktor:ktor-auth:${Versions.ktor}")
                api("io.ktor:ktor-serialization:${Versions.ktor}")
                api("io.ktor:ktor-client-serialization:${Versions.ktor}")
                api("io.ktor:ktor-client-cio:${Versions.ktor}") // TODO check if we want this one (CIO) or another
                api("ch.qos.logback:logback-classic:1.2.3")
                api("org.jetbrains.exposed:exposed-core:${Versions.exposed}")
                api("org.jetbrains.exposed:exposed-dao:${Versions.exposed}")
                api("org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}")
                api("org.jetbrains.exposed:exposed-java-time:${Versions.exposed}")
                implementation("com.zaxxer:HikariCP:${Versions.hikari}")
                api("com.github.ajalt:clikt-multiplatform:${Versions.clikt}")
                api("com.charleskorn.kaml:kaml:${Versions.kaml}")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            kotlin.srcDir("src/commonJvmAndroid/kotlin")
            dependencies {
                api("io.ktor:ktor-server-core:${Versions.ktor}")
                implementation("io.ktor:ktor-server-netty:${Versions.ktor}")
                implementation("io.ktor:ktor-server-sessions:${Versions.ktor}")
                api("io.ktor:ktor-websockets:${Versions.ktor}")
                api("io.ktor:ktor-auth:${Versions.ktor}")
                api("io.ktor:ktor-serialization:${Versions.ktor}")
                api("io.ktor:ktor-client-serialization:${Versions.ktor}")
                api("io.ktor:ktor-client-cio:${Versions.ktor}") // TODO check if we want this one (CIO) or another
                api("ch.qos.logback:logback-classic:1.2.3")
                api("org.jetbrains.exposed:exposed-core:${Versions.exposed}")
                api("org.jetbrains.exposed:exposed-dao:${Versions.exposed}")
                api("org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}")
                api("org.jetbrains.exposed:exposed-java-time:${Versions.exposed}")
                implementation("org.postgresql:postgresql:${Versions.postgresql}")
                implementation("com.zaxxer:HikariCP:${Versions.hikari}")
                api("com.github.ajalt:clikt-multiplatform:${Versions.clikt}")
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

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }
}

tasks.named("compileKotlinJs") {
    this as org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
    kotlinOptions.moduleKind = "umd"
}

noArg {
    annotation("kotlinx.serialization.Serializable")
}

// add sign and publish only if the user is a zakadabar publisher

if (! Versions.isSnapshot && properties["zakadabar.publisher"] != null) {

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

    val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

    val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
        dependsOn(dokkaHtml)
        archiveBaseName.set("core")
        archiveClassifier.set("javadoc")
        from(dokkaHtml.outputDirectory)
    }

    tasks.getByName("build") {
        dependsOn(javadocJar)
    }

    signing {
        useGpgCmd()
        sign(publishing.publications)
    }

    publishing {

        val path = "spxbhuhb/zakadabar-stack"

        repositories {
            maven {
                name = "MavenCentral"
                url = if (Versions.isSnapshot) {
                    uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                } else {
                    uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                }
                credentials {
                    username = (properties["central.user"] ?: System.getenv("CENTRAL_USERNAME")).toString()
                    password = (properties["central.password"] ?: System.getenv("CENTRAL_PASSWORD")).toString()
                }
            }
        }

        publications.withType<MavenPublication>().all {
            artifact(javadocJar.get())
            pom {
                description.set("Kotlin/Ktor based full-stack platform")
                name.set("Zakadabar Stack")
                url.set("https://github.com/$path")
                scm {
                    url.set("https://github.com/$path")
                    connection.set("scm:git:git://github.com/$path.git")
                    developerConnection.set("scm:git:ssh://git@github.com/$path.git")
                }
                licenses {
                    license {
                        name.set("Apache 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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

}
