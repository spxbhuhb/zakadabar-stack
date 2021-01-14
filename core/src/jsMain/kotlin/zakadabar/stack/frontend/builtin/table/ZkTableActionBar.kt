/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.simple.SimpleInput
import zakadabar.stack.frontend.elements.ZkElement

open class ZkTableActionBar() : ZkElement() {

    lateinit var title: String
    var onCreate: (() -> Unit)? = null
    var onSearch: ((String) -> Unit)? = null

    constructor(builder: ZkTableActionBar.() -> Unit) : this() {
        builder()
    }

    override fun init() = build {
        + row {
            if (onCreate != null) {
                + Icons.addBox.complex20 { onCreate?.invoke() }
            }

            + title

            if (onSearch != null) {
                + SimpleInput(onSearch !!)
            }
        }
    }

}