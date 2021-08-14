/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import java.nio.file.Files
import java.nio.file.StandardOpenOption

abstract class Upgrade20210815 : DefaultTask() {

    private val map = listOf(
        "package zakadabar.stack." to "package zakadabar.core.",
        "import zakadabar.stack." to "import zakadabar.core.",
        "import zakadabar.core.backend.module" to "import zakadabar.core.module.module",
        "import zakadabar.core.frontend.builtin.pages.ZkCrudTarget" to "import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget",
        "import zakadabar.core.backend." to "import zakadabar.core."
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