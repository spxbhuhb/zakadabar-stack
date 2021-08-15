/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.dependencies.Versions

plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka")
    signing
    `maven-publish`
    id("upgrade-2021-08-15")
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

dependencies {
    api(project(":core:core"))
}

android {
    compileSdk = Versions.Android.compileSdk
    sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")

    defaultConfig {
        minSdk = Versions.Android.minSdk
        targetSdk = Versions.Android.targetSdk
        versionCode = Versions.Android.versionCode
        versionName = Versions.Android.versionName
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
        archiveBaseName.set("core-android")
        archiveClassifier.set("javadoc")
        from(dokkaHtml.outputDirectory)
    }

    tasks.getByName("build") {
        dependsOn(javadocJar)
    }

    afterEvaluate {

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

            publications {

                create<MavenPublication>("release") {
                    artifactId = "core-android"
                    from(components.getByName("release"))
                    artifact(javadocJar.get())
                    pom {
                        description.set("Kotlin/Ktor based full-stack platform - Android")
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
    }
}
