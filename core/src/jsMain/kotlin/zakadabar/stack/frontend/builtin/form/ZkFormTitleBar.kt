/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form

import zakadabar.stack.frontend.elements.ZkClasses.Companion.zkClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.marginRight
import zakadabar.stack.frontend.elements.plusAssign

// FIXME make titlebar dynamic, when bound to data fields it should update automatically

open class ZkFormTitleBar() : ZkElement() {

    constructor(builder: ZkFormTitleBar.() -> Unit) : this() {
        builder()
    }

    var title: String? = null

    override fun init(): ZkFormTitleBar {
        classList += zkClasses.titleBar

        + row(zkClasses.w100) {

            style { justifyContent = "space-between" }

            + row {

                style { alignItems = "center" }

                title?.let {
                    + div {
                        + it
                    } marginRight 16
                }

            }
        }

        return this
    }

}
