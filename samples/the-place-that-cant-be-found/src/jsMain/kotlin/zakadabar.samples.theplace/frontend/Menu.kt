/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkElement.Companion.buildNew

class Role(val name: String)

val admin = Role("admin")

val Menu = buildNew {

    + column {
        + SimpleButton("Ships") { Ships.openAll() }
        + SimpleButton("Tortuga") { Tortuga.open() }
        + SimpleButton("Singapore") { Singapore.open() }
    }

}