/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util.routing

import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams

data class ViewRequest(
    val path: List<String> = window.location.href.split('/').filter { it.isNotEmpty() },
    val searchParams: URLSearchParams = URLSearchParams(window.location.search),
    val pathParams: MutableMap<String, String> = mutableMapOf()
)
