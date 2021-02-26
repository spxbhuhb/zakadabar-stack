/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.builtin.simple.SimpleInput
import zakadabar.stack.frontend.elements.ZkClasses.Companion.zkClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.marginRight
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.Icons

open class ZkTableTitleBar() : ZkElement() {

    constructor(builder: ZkTableTitleBar.() -> Unit) : this() {
        builder()
    }

    var title: String? = null
    var onCreate: (() -> Unit)? = null
    var onSearch: ((query: String) -> Unit)? = null

    override fun init(): ZkTableTitleBar {
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

            + row {

                style { alignItems = "center" }

                onCreate?.let {
                    + ZkIconButton(Icons.add, round = true) { it.invoke() } marginRight 16
                }

                onSearch?.let {
                    + SimpleInput(onChange = it, enter = true) marginRight 8
                    + ZkIconButton(Icons.search, buttonSize = 24) {
                        this@ZkTableTitleBar[SimpleInput::class].value
                    }
                }
            } marginRight 10
        }


        return this
    }

}
