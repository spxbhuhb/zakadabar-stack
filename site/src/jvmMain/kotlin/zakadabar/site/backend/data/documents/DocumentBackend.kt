/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend.data.documents

import io.ktor.routing.*
import org.slf4j.LoggerFactory
import zakadabar.site.data.DocumentTreeEntry
import zakadabar.site.data.DocumentTreeQuery
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.query.QueryBackend
import zakadabar.stack.util.Executor
import java.io.File

object DocumentBackend : QueryBackend {

    override val namespace = "documents"

    override val logger by lazy { LoggerFactory.getLogger(namespace)!! }

    override fun onInstallRoutes(route: Route) {
        route.query(DocumentTreeQuery::class, ::query)
    }

    private fun query(executor: Executor, query: DocumentTreeQuery): List<DocumentTreeEntry> {
        val result = mutableListOf<DocumentTreeEntry>()

        val root = File(Server.staticRoot).absoluteFile

        root.walk().forEach {
            result += DocumentTreeEntry(it.relativeTo(root).path, emptyList())
        }

        return result
    }

}
