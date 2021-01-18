/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.builtin.button.IconButton
import zakadabar.stack.frontend.builtin.simple.SimpleInput
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.Icons

open class ZkTableActionBar() : ZkElement() {

    lateinit var title: String
    var onCreate: (() -> Unit)? = null
    var onSearch: ((String) -> Unit)? = null

    constructor(builder: ZkTableActionBar.() -> Unit) : this() {
        builder()
    }

    override fun init() = build {
        + row(ZkTableStyles.actionBar) {

            if (onCreate != null) {
                + IconButton(Icons.add) { onCreate?.invoke() } marginRight 16
            }

            + div {
                style { flexGrow = "1" }
                + title
            }

            if (onSearch != null) {
                + SimpleInput(onChange = onSearch !!) marginRight 8
                + IconButton(Icons.search) { onCreate?.invoke() }
            }
        }
    }

}