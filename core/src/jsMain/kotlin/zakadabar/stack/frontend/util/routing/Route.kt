/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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