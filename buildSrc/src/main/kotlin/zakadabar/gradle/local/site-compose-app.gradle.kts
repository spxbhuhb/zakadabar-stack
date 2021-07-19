/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

val distDir = "$buildDir/app/${project.name}-$version-server"

val copyAppStruct by tasks.registering(Copy::class) {
    from("$projectDir/template/app")
    into(distDir)
    include("**")
    exclude("**/.gitignore")

    filter { line: String ->
        line.replace("@version@", "${project.version}")
    }
}

val copyAppLib by tasks.registering(Copy::class) {
    from("$buildDir/libs")
    into("$distDir/lib")
    include("*-${project.version}-all.jar")
}

val copyAppIndex by tasks.registering(Copy::class) {
    from("$buildDir/distributions")
    into("$distDir/var/static")
    include("index.html")
    filter { line: String ->
        line.replace("""src="/${project.name}.js"""", """src="/${project.name}-${project.version}.js"""")
    }
}

val copyAppStatic by tasks.registering(Copy::class) {
    from("$buildDir/distributions")
    into("$distDir/var/static")
    include("**")

    exclude("index.html")
    exclude("*.tar")
    exclude("*.zip")

    rename("${project.name}.js", "${project.name}-${project.version}.js")
}

val copyMarkdown by tasks.registering(Copy::class) {
    from("${rootProject.projectDir}/doc")
    into("$distDir/var/static/doc")
    include("**/*")
    includeEmptyDirs = false
}

val copyAppUsr by tasks.registering(Copy::class) {
    from("$projectDir")
    into("$distDir/usr")
    include("README.md")
    include("LICENSE.txt")
}

val zkBuild by tasks.registering(Zip::class) {
    group = "zakadabar"

    dependsOn(tasks["build"], copyAppStruct, copyAppLib, copyAppStatic, copyAppIndex, copyMarkdown, copyAppUsr)

    archiveFileName.set("${project.name}-${project.version}-server.zip")
    destinationDirectory.set(file("$buildDir/app"))

    from(distDir)
}