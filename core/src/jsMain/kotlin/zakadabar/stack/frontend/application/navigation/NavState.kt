/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application.navigation

import org.w3c.dom.url.URLSearchParams
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.application.Application

/**
 * Stores the current navigation state of the browser window. Created by
 * [Navigation].
 *
 * Check the URLs and/or the Navigation section of the documentation for more information.
 *
 * @property  layout    The layout instance this navigation state uses.
 * @property  module    Shid of the module that provides the Element to use.
 * @property  view      The name of the view in the URL, selects the Element to use.
 * @property  recordId  Id of the record when specified in the URL.
 * @property  query     The query object when specified in the URL.
 */
class NavState(urlPath: String, urlQuery: String) {

    val layout: AppLayout
    val module: String
    val view: String
    val recordId: Long
    val query: Any?

    val target: NavTarget

    init {
        val segments = urlPath.trim('/').split("/")

        // use application home when there are no segments

        if (segments.size == 1 && segments[0].isEmpty()) {

            target = Application.home
            layout = target.layout
            module = target.module
            view = target.name
            recordId = 0
            query = null

        } else {

            val searchParams = URLSearchParams(urlQuery.trim('?'))

            layout = Application.layouts.find { it.name == segments[0] } ?: throw IllegalStateException("missing layout $segments[0]")
            module = segments[1]
            view = segments[2]

            recordId = searchParams.get("id")?.toLong() ?: 0

            query = searchParams.get("query")?.let {
                val dataType = "$module/$view"
                val dtoFrontend = FrontendContext.dtoFrontends[dataType] ?: throw IllegalStateException("missing dto frontend for $dataType")
                dtoFrontend.decodeQuery(dataType, it)
            }

            target = layout.navTargets.find { it.accepts(this) } ?: throw IllegalStateException("missing nav target, path=$urlPath query=$urlQuery")
        }
    }

}
