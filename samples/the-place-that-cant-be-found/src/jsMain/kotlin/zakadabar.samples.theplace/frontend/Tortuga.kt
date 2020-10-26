/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.stack.frontend.builtin.navigation.NavState
import zakadabar.stack.frontend.builtin.navigation.Route
import zakadabar.stack.frontend.elements.ComplexElement

object Tortuga : Route() {

    override fun element(newState: NavState) = ComplexElement() build {
        + "Tortuga"
    }

}
