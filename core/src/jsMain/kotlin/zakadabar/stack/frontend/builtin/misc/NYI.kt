/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc

import zakadabar.stack.frontend.builtin.ZkElement

class NYI(private val message: String = "not yet implemented") : ZkElement() {

    override fun onCreate() {
        this.innerText = message
    }

}