/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util.routing

import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.util.PublicApi

@PublicApi
fun Route.route(path: String, builder: Route.() -> Route): Route {
    val route = Route(toSegments(path))
    route.builder()
    this.children += route
    return route
}

@PublicApi
fun Route.handle(path: String, handler: (request: ViewRequest) -> ZkElement): Route {
    val route = Route(toSegments(path), handler)
    this.children += route
    return route
}

fun toSegments(path: String): List<PathSegment> =
    path.splitToSequence('/').mapNotNull {
        when {
            it.isEmpty() -> null
            it.startsWith("{") && it.endsWith("}") -> ParameterSegment(it.removeSurrounding("{", "}"))
            else -> ConstantSegment(it)
        }
    }.toList()


interface PathSegment {
    fun matches(request: ViewRequest, position: Int): Boolean
}

class ParameterSegment(val name: String) : PathSegment {

    override fun matches(request: ViewRequest, position: Int): Boolean {
        if (request.path.size <= position) return false
        return true
    }

}

class ConstantSegment(val path: String) : PathSegment {

    override fun matches(request: ViewRequest, position: Int): Boolean {
        if (request.path.size <= position) return false
        return (request.path[position] == path)
    }

}