/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import org.w3c.dom.url.URLSearchParams
import zakadabar.stack.frontend.FrontendContext

/**
 * Stores the current navigation state of the browser window. Created by
 * [Navigation].
 *
 * Check the URLs and/or the Navigation section of the documentation for more information.
 *
 * @property  module    Shid of the module that provides the Element to use.
 * @property  view      The name of the view in the URL, selects the Element to use.
 * @property  recordId  Id of the record when specified in the URL.
 * @property  query     The query object when specified in the URL.
 */
class NavState(urlPath: String, urlQuery: String) {

    val module: String
    val view: String
    val recordId: Long
    val query: Any?

    init {
        val segments = urlPath.trim('/').split("/", limit = 2)

        // use application home when there are no segments

        if (segments.size < 2) {

            module = ""
            view = ""
            recordId = 0
            query = null

        } else {

            val searchParams = URLSearchParams(urlQuery.trim('?'))

            module = segments[0]
            view = "/" + segments[1]

            recordId = searchParams.get("id")?.toLong() ?: 0

            query = searchParams.get("query")?.let {
                val dataType = "$module/$view"
                val dtoFrontend = FrontendContext.dtoFrontends[dataType] ?: throw IllegalStateException("missing dto frontend for $dataType")
                dtoFrontend.decodeQuery(dataType, it)
            }
        }
    }

}
