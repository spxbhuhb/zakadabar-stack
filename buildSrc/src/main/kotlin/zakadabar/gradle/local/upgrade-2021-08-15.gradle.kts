/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import java.nio.file.Files
import java.nio.file.StandardOpenOption

abstract class Upgrade20210815 : DefaultTask() {

    private val map = listOf(
        //"package zakadabar.stack." to "package zakadabar.core.",
        "import zakadabar.stack." to "import zakadabar.core.",
        "import zakadabar.core.backend.module" to "import zakadabar.core.module.module",
        "import zakadabar.core.frontend.builtin.pages.ZkCrudTarget" to "import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget",
        "import zakadabar.core.backend.RoutedModule" to "import zakadabar.core.route.RoutedModule",
        "import zakadabar.core.backend." to "import zakadabar.core.",
        "import zakadabar.core.exceptions." to "import zakadabar.core.exception.",
        "import zakadabar.core.resources." to "import zakadabar.core.resource.",
        "import zakadabar.core.server" to "import zakadabar.core.server.server",
        "import zakadabar.core.Server" to "import zakadabar.core.server.Server",
        "import zakadabar.core.backend.ktor." to "import zakadabar.core.server.ktor.",
        "import zakadabar.core.backend.util." to "zakadabar.core.util.",
        "import zakadabar.core.backend.exposed." to "import zakadabar.core.persistence.exposed.",
        "import zakadabar.core.backend.sql." to "import zakadabar.core.persistence.sql.",
        "import zakadabar.core.backend.builtin." to "import zakadabar.core.server.",
        "import zakadabar.core.backend.testing." to "import zakadabar.core.testing.",
    )

    @TaskAction
    private fun upgrade() {
        File(this.project.projectDir.absolutePath, "src").walk().forEach {
            if (! it.isFile) return@forEach
            if (! it.name.endsWith(".kt")) return@forEach

            val content = Files.readAllBytes(it.toPath()).decodeToString()

            var newContent = content
            for (mapping in map) {
                newContent = newContent.replace(mapping.first, mapping.second)
            }



            Files.write(it.toPath(), newContent.toByteArray(), StandardOpenOption.TRUNCATE_EXISTING)
        }
    }

}

val zkUpgrade20210815 by tasks.register<Upgrade20210815>("zkUpgrade20210815") {
    group = "zakadabar"
}