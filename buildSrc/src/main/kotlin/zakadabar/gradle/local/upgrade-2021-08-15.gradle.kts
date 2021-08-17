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
        "import zakadabar.core.exceptions." to "import zakadabar.core.exception.",
        "import zakadabar.core.resources." to "import zakadabar.core.resource.",
        "import zakadabar.core.backend." to "import zakadabar.core.",
        "import zakadabar.core.RoutedModule" to "import zakadabar.core.route.RoutedModule",
        "import zakadabar.core.server" to "import zakadabar.core.server.server",
        "import zakadabar.core.server.server." to "import zakadabar.core.server.", // one above is recursive
        "import zakadabar.core.Server" to "import zakadabar.core.server.Server",
        "import zakadabar.core.ktor." to "import zakadabar.core.server.ktor.",
        "import zakadabar.core.exposed." to "import zakadabar.core.persistence.exposed.",
        "import zakadabar.core.sql." to "import zakadabar.core.persistence.sql.",
        "import zakadabar.core.builtin." to "import zakadabar.core.server.",
        "import zakadabar.core.data.action." to "import zakadabar.core.data.",
        "import zakadabar.core.data.entity." to "import zakadabar.core.data.",
        "import zakadabar.core.data.query." to "import zakadabar.core.data.",
        "import zakadabar.core.data.schema." to "import zakadabar.core.schema.",
        "import zakadabar.core.data.builtin.account." to "import zakadabar.core.authorize.",
        "import zakadabar.core.data.builtin.authorize." to "import zakadabar.core.authorize.",
        "import zakadabar.core.data.builtin.resources." to "import zakadabar.core.setting.",
        "import zakadabar.core.data.builtin.settings." to "import zakadabar.core.server.",
        "import zakadabar.core.data.builtin.misc.ServerDescription" to "import zakadabar.core.server.ServerDescription",
        "import zakadabar.core.data.builtin.misc.Secret" to "import zakadabar.core.data.Secret",
        "import zakadabar.core.data.builtin.misc.StringPair" to "import zakadabar.core.data.StringPair",
        "import zakadabar.core.data.builtin.misc.ActionStatusBo" to "import zakadabar.core.data.ActionStatusBo",
        "ActionStatusBo" to "ActionStatus",
        "import zakadabar.core.data.builtin." to "import zakadabar.core.data.",
        "import zakadabar.core.data.CommBase" to "import zakadabar.core.comm.CommBase",
        "import zakadabar.core.frontend.application." to "import zakadabar.core.browser.application.",
        "import zakadabar.core.frontend.builtin." to "import zakadabar.core.browser.",
        "import zakadabar.core.frontend.resources." to "import zakadabar.core.resource.",
        "import zakadabar.core.frontend.util" to "import zakadabar.core.browser.util",
        "import zakadabar.core.browser.form.fields" to "import zakadabar.core.browser.field",
        "import zakadabar.core.browser.form.structure" to "import zakadabar.core.browser.form",
        "import zakadabar.core.browser.layout.slider" to "import zakadabar.core.browser.slider",
        "import zakadabar.core.browser.layout.tabcontainer" to "import zakadabar.core.browser.tabcontainer",
        "import zakadabar.core.browser.pages" to "import zakadabar.core.browser.page",
        "import zakadabar.lib.accounts.frontend" to "import zakadabar.lib.accounts.browser",
        "import zakadabar.lib.accounts.data.AuthHttpClient" to "import zakadabar.lib.accounts.comm.AuthHttpClient",
        "import zakadabar.lib.accounts.backend.bl" to "import zakadabar.lib.accounts.business",
        "import zakadabar.lib.accounts.backend.pa" to "import zakadabar.lib.accounts.persistence",
        "import zakadabar.lib.accounts.backend.testing" to "import zakadabar.lib.accounts.testing",
        "import zakadabar.lib.accounts.server.ktor" to "import zakadabar.lib.accounts.server.ktor",
        "zakadabar.lib.accounts.backend.install" to "zakadabar.lib.accounts.install",
        "import zakadabar.lib.blobs.backend.BlobBlBase" to "import zakadabar.lib.blobs.business.BlobBlBase",
        "import zakadabar.lib.blobs.backend.BlobExposedPa" to "import zakadabar.lib.blobs.persistence.BlobExposedPa",
        "import zakadabar.lib.blobs.backend.BlobExposedTable" to "import zakadabar.lib.blobs.persistence.BlobExposedTable",
        "zakadabar.lib.i18n.frontend.install" to "zakadabar.lib.i18n.browser.install",
        "import zakadabar.lib.i18n.frontend.LocaleCrud" to "import zakadabar.lib.i18n.browser.LocaleCrud",
        "import zakadabar.lib.i18n.frontend.TranslationCrud" to "import zakadabar.lib.i18n.browser.TranslationCrud",
        "zakadabar.lib.i18n.backend.install()" to "zakadabar.lib.i18n.install()"
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