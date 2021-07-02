/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

val syncBuildInfo by tasks.registering(Copy::class) {
    from("$projectDir/template/zkBuild")
    inputs.property("version", project.version)
    filter { line: String ->
        line.replace("@version@", "${project.version}")
            .replace("@projectName@", project.name)
            .replace("@stackVersion@", "${rootProject.childProjects["core"]?.version ?: "unknown"}")
    }
    into("$projectDir/src/jvmMain/resources")
}

tasks.named<Copy>("jvmProcessResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks["compileKotlinJvm"].dependsOn(syncBuildInfo)
