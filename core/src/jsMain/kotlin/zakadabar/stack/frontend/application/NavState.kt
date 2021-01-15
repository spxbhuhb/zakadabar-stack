/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import org.w3c.dom.url.URLSearchParams

/**
 * Stores the current navigation state of the browser window.
 *
 * @property  viewName      The name of the view in the URL, selects the Element to use.
 * @property  recordId  Id of the record when specified in the URL.
 * @property  query     The query object when specified in the URL.
 */
class NavState(val urlPath: String, val urlQuery: String) {

    val viewName: String
    val recordId: Long
    val query: Any?

    init {
        val segments = urlPath.trim('/').split("/")

        // use application home when there are no segments

        if (segments.isEmpty()) {

            viewName = ""
            recordId = 0
            query = null

        } else {

            val searchParams = URLSearchParams(urlQuery.trim('?'))

            viewName = segments[0]

            recordId = searchParams.get("id")?.toLong() ?: 0

            query = searchParams.get("query")?.let {
                val dataType = viewName
//                val dtoFrontend = Application.dtoFrontends[dataType] ?: throw IllegalStateException("missing dto frontend for $dataType")
//                dtoFrontend.decodeQuery(dataType, it)
            }
        }
    }

    override fun toString(): String {
        return "urlPath=$urlPath, urlQuery=$urlQuery, viewName=$viewName, recordId=$recordId, query=$query"
    }
}
