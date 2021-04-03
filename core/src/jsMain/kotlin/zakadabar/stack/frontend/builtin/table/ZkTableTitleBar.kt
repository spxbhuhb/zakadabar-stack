/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles
import zakadabar.stack.frontend.builtin.standalone.ZkStandaloneInput
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarStyles
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.marginRight
import zakadabar.stack.frontend.util.plusAssign

open class ZkTableTitleBar() : ZkElement() {

    constructor(builder: ZkTableTitleBar.() -> Unit) : this() {
        builder()
    }

    var title: String? = null
    var onAddRow: (() -> Unit)? = null
    var onSearch: ((query: String) -> Unit)? = null
    var onExportCsv: (() -> Unit)? = null

    override fun onCreate() {
        classList += ZkTitleBarStyles.titleBar

        + row(ZkLayoutStyles.w100) {

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

                onAddRow?.let {
                    + ZkIconButton(ZkIcons.add, round = true) { it.invoke() } marginRight 16
                }

                onExportCsv?.let {
                    + ZkIconButton(ZkIcons.fileDownload, round = true) { it.invoke() } marginRight 16
                }

                onSearch?.let {
                    + ZkStandaloneInput(onChange = it, enter = true) marginRight 8
                    + ZkIconButton(ZkIcons.search, buttonSize = 24) {
                        val value = this@ZkTableTitleBar[ZkStandaloneInput::class].value
                        it.invoke(value)
                    }
                }

            } marginRight 10
        }
    }

}
