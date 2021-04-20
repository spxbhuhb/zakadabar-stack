/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend.data.content

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.datetime.Instant
import org.slf4j.LoggerFactory
import zakadabar.site.data.ContentEntry
import zakadabar.site.data.ContentQuery
import zakadabar.stack.backend.custom.CustomBackend
import zakadabar.stack.backend.data.query.QueryBackend
import zakadabar.stack.util.Executor
import java.io.File

object ContentBackend : CustomBackend(), QueryBackend {

    override val namespace = ContentQuery.namespace

    override val logger by lazy { LoggerFactory.getLogger(namespace) !! }

    private val root = File("./content") // FIXME replace hard coded file path ./doc

    private val pattern = Regex("[a-zA-Z0-9/\\.]+\\.[a-zA-Z0-9]*")

    override fun onInstallRoutes(route: Route) {
        route.query(ContentQuery::class, ::query)

        route.get("$namespace/{name...}") {
            val path = call.parameters.getAll("name")?.joinToString("/") ?: throw NotFoundException()
            if (! path.matches(pattern) || ".." in path) throw BadRequestException("invalid name")
            call.respondFile(File(root, path))
        }
    }

    @Suppress("UNUSED_PARAMETER") // parameters are used by automatic mapping
    private fun query(executor: Executor, query: ContentQuery): List<ContentEntry> {
        val result = mutableListOf<ContentEntry>()

        root.walk().forEach {
            if (it.name.endsWith(".md")) {
                result += ContentEntry(it.nameWithoutExtension, it.relativeTo(root).path, Instant.fromEpochSeconds(it.lastModified()))
            }
        }

        return result
    }

}
