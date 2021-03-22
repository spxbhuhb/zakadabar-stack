/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.input.checkboxlist

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.util.PublicApi

open class ZkCheckboxList<T>(
    open val items: List<ZkCheckboxListItem<T>> = emptyList()
) : ZkElement() {

    @PublicApi
    var fieldGridColumnTemplate: String = "150px 1fr"

    @PublicApi
    var fieldGridRowTemplate: String = "max-content"

    override fun onCreate() {
        + grid(style = "grid-template-columns: $fieldGridColumnTemplate; gap: 0; grid-auto-rows: $fieldGridRowTemplate") {
            items.forEach { item ->
                + div(ZkFormStyles.fieldLabel) {
                    + item.label
                }

                + div(ZkFormStyles.checkbox) {
                    val checkbox = document.createElement("input") as HTMLInputElement
                    checkbox.type = "checkbox"
                    checkbox.checked = item.selected
                    + checkbox

                    on(checkbox, "click") {
                        item.selected = checkbox.checked
                    }
                }
            }
        }
    }
}