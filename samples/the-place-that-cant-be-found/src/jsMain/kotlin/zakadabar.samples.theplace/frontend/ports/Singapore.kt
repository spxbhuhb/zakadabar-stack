/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend.ports

import zakadabar.samples.theplace.ThePlace
import zakadabar.stack.frontend.elements.ZkPage

object Singapore : ZkPage(ThePlace.shid, "/singapore") {

    override fun init() = build {
        + "Singapore"
    }

}