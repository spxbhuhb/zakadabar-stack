/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.misc

import zakadabar.core.browser.ZkElement

class NYI(private val message: String = "not yet implemented") : ZkElement() {

    override fun onCreate() {
        style {
            paddingLeft = "10px"
            paddingRight = "10px"
        }
        this.innerText = message
    }

}