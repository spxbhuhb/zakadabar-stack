/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend.data.documents

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import org.slf4j.LoggerFactory
import zakadabar.site.data.DocumentTreeEntry
import zakadabar.site.data.DocumentTreeQuery
import zakadabar.stack.backend.custom.CustomBackend
import zakadabar.stack.backend.data.query.QueryBackend
import zakadabar.stack.util.Executor
import java.io.File

object DocumentBackend : CustomBackend(), QueryBackend {

    override val namespace = "documents"

    override val logger by lazy { LoggerFactory.getLogger(namespace) !! }

    private val root = File("./doc") // FIXME replace hard coded file path ./doc

    private val pattern = Regex("[\\w\\\\]+\\.[\\w]*")

    override fun onInstallRoutes(route: Route) {
        route.query(DocumentTreeQuery::class, ::query)

        route.get("content/{name}") {
            val name = call.parameters["name"] ?: throw NotFoundException()
            if (! name.matches(pattern)) throw BadRequestException("invalid name")
            call.respondFile(File(root, name))
        }
    }

    private fun query(executor: Executor, query: DocumentTreeQuery): List<DocumentTreeEntry> {
        val result = mutableListOf<DocumentTreeEntry>()

        root.walk().forEach {
            if (it.name.endsWith(".md")) {
                result += DocumentTreeEntry(it.nameWithoutExtension, it.relativeTo(root).path)
            }
        }

        return result
    }

}
