/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.port

import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.elements.ZkPage

object Tortuga : ZkPage() {

    override fun init() = build {
        + Strings.tortuga
    }

}
