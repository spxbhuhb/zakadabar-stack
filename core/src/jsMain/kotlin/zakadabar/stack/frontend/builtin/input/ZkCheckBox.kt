/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.input

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.resources.ZkIconSource

open class ZkCheckBox(
    val iconSource: ZkIconSource
) : ZkElement() {

    open val checkbox = document.createElement("input") as HTMLInputElement
    open val label = document.createElement("label") as HTMLLabelElement

    open var disabled: Boolean
        get() = checkbox.disabled
        set(value) {
            checkbox.disabled = value
        }

    open var checked: Boolean
        get() = checkbox.checked
        set(value) {
            checkbox.checked = value
        }

    override fun onCreate() {
        checkbox.id = "${this.id}-checkbox"
        checkbox.type = "checkbox"
        checkbox.className = zkInputStyles.checkBox
        + checkbox

        label.htmlFor = checkbox.id
        label.className = zkInputStyles.checkboxLabel
        + label

        label.innerHTML = iconSource.svg(18)
    }

}