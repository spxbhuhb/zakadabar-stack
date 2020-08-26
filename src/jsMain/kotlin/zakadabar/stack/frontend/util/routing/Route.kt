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

class Route(
    private val segments: List<PathSegment>,
    private val handler: ((request: ViewRequest) -> ComplexElement?)? = null
) {
    val children = mutableListOf<Route>()

    fun route(request: ViewRequest, position: Int, parameters: MutableList<Pair<String, String>>): ComplexElement? {
        var currentPosition = position

        for (segment in segments) {
            if (! segment.matches(request, currentPosition)) return null

            if (segment is ParameterSegment) {
                parameters += segment.name to request.path[currentPosition]
            }

            currentPosition ++
        }

        if (currentPosition == request.path.size && handler != null) {
            request.pathParams.putAll(parameters)
            return handler.invoke(request)
        }

        for (child in children) {
            // creating a new mutable list is intentional, this ensures that non-matching
            // routes do not add parameters to the one that match
            val view = child.route(request, currentPosition, parameters.toMutableList())
            if (view != null) return view
        }

        return null
    }

    companion object {
        fun route(path: String, builder: Route.() -> Route): Route {
            val route = Route(toSegments(path))
            route.builder()
            return route
        }

        fun handle(path: String, handler: (request: ViewRequest) -> ComplexElement?): Route {
            return Route(toSegments(path), handler)
        }
    }
}