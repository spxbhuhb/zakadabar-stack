/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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