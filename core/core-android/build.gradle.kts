/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.Versions
import zakadabar.gradle.config
import zakadabar.gradle.isPublishing
import zakadabar.gradle.manifestAndDokka

plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka")
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

dependencies {
    api(project(":core:core"))
}

android {
    compileSdk = Versions.Android.compileSdk
    sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")

    kotlinOptions {
        languageVersion = "1.6"
    }

    defaultConfig {
        minSdk = Versions.Android.minSdk
        targetSdk = Versions.Android.targetSdk
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/hk2-locator/default")
        exclude("license/*")
        exclude("license/LICENSE.dom-documentation.txt")
        exclude("META-INF/io.netty.versions.properties")
        exclude("META-INF/INDEX.LIST")
    }
}

if (project.isPublishing) {

    manifestAndDokka(tasks)

    afterEvaluate {
        signing { config(publishing.publications) }

        publishing {

            config(project)

            val path = "spxbhuhb/zakadabar-stack"

            publications {

                create<MavenPublication>("release") {
                    artifactId = "core-android"
                    from(components.getByName("release"))
                    artifact(tasks["javadocJar"])
                    pom {
                        description.set("Kotlin/Ktor based full-stack platform")
                        name.set("Zakadabar Core Android")
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
    }
}