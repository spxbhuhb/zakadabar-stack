/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigation

import org.w3c.dom.url.URLSearchParams
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.builtin.layout.AppLayout

/**
 * Stores the current navigation state of the browser window. Created by
 * [Navigation].
 *
 * Check the URLs and/or the Navigation section of the documentation for more information.
 *
 * @property  layout    The layout instance this navigation state uses.
 * @property  module    Shid of the module that provides the Element to use.
 * @property  viewName  The name of the view in the URL, selects the Element to use.
 * @property  recordId  Id of the record when specified in the URL.
 * @property  query     The query object when specified in the URL.
 */
class NavState(urlPath: String, urlQuery: String) {

    val layout: AppLayout
    val module: String
    val viewName: String
    val recordId: Long?
    val query: Any?

    /**
     * Path format: /layout/module/view.subview?id=12&q={...}
     */
    init {
        val segments = urlPath.trim('/').split("/")

        layout = FrontendContext.layouts.find { it.name == segments[0] } ?: throw IllegalStateException("missing layout $segments[0]")
        module = segments[1]
        viewName = segments[2]

        val searchParams = URLSearchParams(urlQuery.trim('?'))

        recordId = searchParams.get("id")?.toLong()

        query = searchParams.get("q")?.let {
            val dataType = "$module/${viewName.substringBefore('.')}"

            val dtoFrontend = FrontendContext.dtoFrontends[dataType] ?: throw IllegalStateException("missing dto frontend for $dataType")
            dtoFrontend.decodeQuery(dataType, it)
        }
    }
}
