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
import zakadabar.stack.util.PublicApi

@PublicApi
fun Route.route(path: String, builder: Route.() -> Route): Route {
    val route = Route(toSegments(path))
    route.builder()
    this.children += route
    return route
}

@PublicApi
fun Route.handle(path: String, handler: (request: ViewRequest) -> ComplexElement): Route {
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