/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend.data.content

import io.ktor.http.content.*
import io.ktor.routing.*
import kotlinx.datetime.Instant
import zakadabar.site.data.ContentBackendSettings
import zakadabar.site.data.ContentEntry
import zakadabar.site.data.ContentQuery
import zakadabar.stack.backend.custom.CustomBackend
import zakadabar.stack.backend.data.builtin.resources.setting
import zakadabar.stack.backend.data.query.QueryBackend
import zakadabar.stack.util.Executor
import java.io.File

object ContentBackend : CustomBackend(), QueryBackend {

    private val settings by setting<ContentBackendSettings>("zakadabar.site.content")

    override val namespace = ContentQuery.dtoNamespace

    override fun onInstallRoutes(route: Route) {
        route.query(ContentQuery::class, ::query)
    }

    override fun onInstallStatic(route: Route) {
        with(route) {
            static(namespace) {
                staticRootFolder = File(settings.root)
                files(".")
            }
        }
    }


    @Suppress("UNUSED_PARAMETER") // parameters are used for automatic mapping
    private fun query(executor: Executor, query: ContentQuery): List<ContentEntry> {
        val result = mutableListOf<ContentEntry>()

        val root = File(settings.root)

        root.walk().forEach {
            if (it.name.endsWith(".md")) {
                result += ContentEntry(it.nameWithoutExtension, it.relativeTo(root).path, Instant.fromEpochSeconds(it.lastModified()))
            }
        }

        return result
    }

}
