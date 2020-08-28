/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util.routing

import zakadabar.stack.frontend.elements.ComplexElement

/**
 * A router for a Single Page Application. Handles browser history, navigation
 * intents and selection of the view to show.
 */
class Router {

    private val routes = mutableListOf<Route>()

    operator fun plusAssign(route: Route) {
        routes += route
    }

    fun route(): ComplexElement? = route(ViewRequest())

    fun route(request: ViewRequest): ComplexElement? {

        for (route in routes) {
            val view = route.route(request, 0, mutableListOf())
            if (view != null) return view
        }

        return null
    }

}