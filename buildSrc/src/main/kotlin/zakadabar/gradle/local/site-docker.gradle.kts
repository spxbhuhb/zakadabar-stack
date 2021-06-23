/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

val distDir = "$buildDir/app/${project.name}-$version-server"

abstract class DockerPrepareTask : DefaultTask() {

    private val rootDir: String = this.project.projectDir.absolutePath
    private val buildDir: String = this.project.buildDir.path
    private val version: String = project.version.toString()

    @TaskAction
    private fun prepareDocker() {
        Files.createDirectories(Paths.get(buildDir, "docker/local"))
        dockerFile()
    }

    private fun dockerFile() {
        val fromPath = Paths.get(rootDir, "template/docker/Dockerfile")
        val toPath = Paths.get(buildDir, "docker/Dockerfile")

        val content = String(Files.readAllBytes(fromPath), StandardCharsets.UTF_8)

        val newContent = content
            .replace("@projectName@", project.name)
            .replace("@version@", version)

        Files.write(toPath, newContent.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
    }

}

val zkDockerPrepare by tasks.register<DockerPrepareTask>("zkDockerPrepare") {
    group = "zakadabar"
}

val zkDockerCopy by tasks.registering(Copy::class) {
    from(distDir)
    into("$buildDir/docker/local/${project.name}")
    include("**")
}