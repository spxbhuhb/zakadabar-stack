/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.builtin.navigation.Route
import zakadabar.stack.frontend.elements.ComplexElement

open class AppLayout(val name : String) : ComplexElement() {

    val routes = mutableListOf<Route>()

}