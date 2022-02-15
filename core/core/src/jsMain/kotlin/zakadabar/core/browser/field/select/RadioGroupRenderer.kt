/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field.select

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.field.ZkSelectBaseV2
import zakadabar.core.browser.input.ZkRadioButton
import zakadabar.core.resource.localizedStrings

open class RadioGroupRenderer<VT, FT : ZkSelectBaseV2<VT, FT>> : SelectRenderer<VT, FT> {

    override lateinit var field: ZkSelectBaseV2<VT, FT>

    open var container = ZkElement()

    override fun readOnly(value: Boolean) {
        container.find<ZkRadioButton>().forEach {
            it.readOnly = true
        }
    }

    override fun buildFieldValue() {
        with(field) {
            + container css context.styles.radioGroupContainer
        }
    }

    override fun render(value: VT?, hide : Boolean) {
        container.clear()

        if (! field.needsMandatoryMark()) {
            container += ZkRadioButton(
                group = "radio-${field.id}",
                text = localizedStrings.notSelected,
                checked = value == null,
                readOnly = field.readOnly,
                onChange = { field.update(items, null, true) }
            ) css context.styles.radioGroupItem
        }

        items.forEach { item ->
            container += ZkRadioButton(
                group = "radio-${field.id}",
                text = item.second,
                checked = item.first == value,
                readOnly = field.readOnly,
                onChange = { field.update(items, item, true) }
            ) css context.styles.radioGroupItem
        }
    }

    override fun focusValue() {
        container.firstOrNull<ZkRadioButton>()?.focus()
    }

}