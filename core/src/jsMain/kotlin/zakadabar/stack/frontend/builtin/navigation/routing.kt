/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigation

import zakadabar.stack.frontend.builtin.desktop.entity.EntityRecordFrontend
import zakadabar.stack.frontend.builtin.util.NYI
import zakadabar.stack.frontend.data.DtoFrontend
import zakadabar.stack.frontend.elements.SimpleElement

class Routing {

}

class RoutingEntry {

}

fun test() {
    routing {
        home { NYI() }
        notFound { NYI() }
        unauthorized { NYI() }
        crud { EntityRecordFrontend }
        page { NYI() }
    }
}

fun routing(build: Routing.() -> Unit): Routing {
    val r = Routing()
    r.build()
    return r
}

fun Routing.home(build: () -> SimpleElement): RoutingEntry {
    return RoutingEntry()
}

fun Routing.crud(build: () -> DtoFrontend<*>): RoutingEntry {
    return RoutingEntry()
}

fun Routing.page(build: () -> SimpleElement): RoutingEntry {
    return RoutingEntry()
}

fun Routing.notFound(build: () -> SimpleElement): RoutingEntry {
    return RoutingEntry()
}

fun Routing.unauthorized(build: () -> SimpleElement): RoutingEntry {
    return RoutingEntry()
}