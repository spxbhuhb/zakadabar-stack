/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarStyles
import zakadabar.stack.frontend.util.marginRight
import zakadabar.stack.frontend.util.plusAssign

// FIXME make titlebar dynamic, when bound to data fields it should update automatically

@Deprecated("use ZkApplication.title instead")
open class ZkFormTitleBar() : ZkElement() {

    constructor(builder: ZkFormTitleBar.() -> Unit) : this() {
        builder()
    }

    var title: String? = null

    override fun onCreate() {
        classList += ZkTitleBarStyles.appTitleBar

        + row(zkLayoutStyles.w100) {

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
    }

}
