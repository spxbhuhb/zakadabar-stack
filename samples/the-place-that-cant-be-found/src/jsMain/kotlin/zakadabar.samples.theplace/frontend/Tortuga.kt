/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.samples.theplace.ThePlace
import zakadabar.stack.frontend.elements.ZkPage

object Tortuga : ZkPage(ThePlace.shid, "/tortuga") {

    override fun init() = build {
        + "Tortuga"
    }

}
