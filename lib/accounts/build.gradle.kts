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
version = "2021.6.4"

val isSnapshot = version.toString().contains("SNAPSHOT")

noArg {
    annotation("kotlinx.serialization.Serializable")
}

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    js {
        nodejs()
    }

    sourceSets["commonMain"].dependencies {
        implementation(project(":core"))
    }

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
    archiveBaseName.set("accounts")
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
                description.set("Accounts plug-and-play module for Zakadabar.")
                name.set("Zakadabar Lib:Accounts")
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