/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.input

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.form.zkFormStyles
import zakadabar.core.resource.ZkIcons
import zakadabar.core.util.PublicApi

/**
 * Displays a list of items as checkboxes, each item may be switched on/off individually.
 * Useful when you don't know how many flags you have.
 */
open class ZkCheckboxList<T>(
    open val items: List<ZkCheckboxListItem<T>> = emptyList()
) : ZkElement() {

    @PublicApi
    var fieldGridColumnTemplate: String = "minmax(150px, max-content) 1fr"

    @PublicApi
    var fieldGridRowTemplate: String = "max-content"

    override fun onCreate() {
        + grid(style = "grid-template-columns: $fieldGridColumnTemplate; gap: 0; grid-auto-rows: $fieldGridRowTemplate") {
            items.forEach { item ->
                + div(zkFormStyles.fieldLabel) {
                    + item.label
                }

                + div(zkInputStyles.checkboxList) {
                    + ZkCheckBox(ZkIcons.check, item.selected) {
                        item.selected = it
                    }
                }
            }
        }
    }
}