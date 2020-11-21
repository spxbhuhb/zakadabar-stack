/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.port

import zakadabar.demo.Demo
import zakadabar.stack.frontend.elements.ZkPage

object Tortuga : ZkPage(Demo.shid, "/tortuga") {

    override fun init() = build {
        + "Tortuga"
    }

}
