/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.dokka")
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = "2021.5.4.2"

val isSnapshot = version.toString().contains("SNAPSHOT")

// common
val ktorVersion = "1.4.0"
val coroutinesVersion = "1.3.9"
val serializationVersion = "1.0.0-RC2"
val datetimeVersion = "0.1.1"
val cliktVersion = "2.8.0"

// jvm
val exposedVersion = "0.26.2"
val postgresqlVersion = "42.2.14"
val hikariVersion = "3.4.5"
val kamlVersion = "0.19.0"

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
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
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
                implementation("io.ktor:ktor-server-sessions:$ktorVersion")
                api("io.ktor:ktor-websockets:$ktorVersion")
                api("io.ktor:ktor-auth:$ktorVersion")
                api("io.ktor:ktor-serialization:$ktorVersion")
                api("io.ktor:ktor-client-serialization:$ktorVersion")
                api("io.ktor:ktor-client-cio:$ktorVersion") // TODO check if we want this one (CIO) or another
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
    }
}

tasks.named("compileKotlinJs") {
    this as org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
    kotlinOptions.moduleKind = "umd"
}

noArg {
    annotation("kotlinx.serialization.Serializable")
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

// add sign and publish only if the user is a zakadabar publisher

if (properties["zakadabar.publisher"] != null) {

    signing {
        useGpgCmd()
        sign(publishing.publications)
    }

    publishing {

        val path = "spxbhuhb/zakadabar-stack"

        repositories {
            maven {
                name = "MavenCentral"
                url = if (isSnapshot) {
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
