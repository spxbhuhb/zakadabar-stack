/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import java.nio.file.Files
import java.nio.file.StandardOpenOption

abstract class Upgrade20210815 : DefaultTask() {

    @TaskAction
    private fun upgrade() {
        File(this.project.projectDir.absolutePath, "src").walk().forEach {
            if (! it.isFile) return@forEach
            if (! it.name.endsWith(".kt")) return@forEach

            val content = Files.readAllBytes(it.toPath()).decodeToString()

            val newContent = content
                .replace("package zakadabar.stack.", "package zakadabar.core.")
                .replace("import zakadabar.stack.", "import zakadabar.core.")

            Files.write(it.toPath(), newContent.toByteArray(), StandardOpenOption.TRUNCATE_EXISTING)
        }
    }

}

val zkUpgrade20210815 by tasks.register<Upgrade20210815>("zkUpgrade20210815") {
    group = "zakadabar"
}