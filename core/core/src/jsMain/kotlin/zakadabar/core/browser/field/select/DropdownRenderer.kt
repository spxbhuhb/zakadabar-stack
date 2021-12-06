/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.field.select

import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.field.ZkSelectBaseV2
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.browser.popup.ZkPopUp
import zakadabar.core.browser.util.escape
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.localizedStrings

open class DropdownRenderer<VT, FT : ZkSelectBaseV2<VT, FT>> : SelectRenderer<VT,FT> {

    companion object {
        private const val DATASET_KEY = "value"
    }

    override lateinit var field: ZkSelectBaseV2<VT, FT>

    open lateinit var container: HTMLElement

    open lateinit var arrow: ZkElement

    open lateinit var itemList : ZkPopUp

    open val selectedOption = ZkElement()

    override fun readOnly(value: Boolean) {
        val cl = container.firstElementChild?.classList

        if (value) {
            cl?.plusAssign(context.styles.disabledSelect)
            arrow.hide()
        } else {
            cl?.minusAssign(context.styles.disabledSelect)
            arrow.show()
        }
    }

    override fun onCreate() {
        itemList = ZkPopUp { + context.styles.selectOptionList }

        itemList.element.tabIndex = 0

        itemList.on("click") {
            it as MouseEvent

            val target = it.target
            if (target !is HTMLElement) return@on

            val entryIdString = target.dataset[DATASET_KEY]
            val entryId = if (entryIdString.isNullOrEmpty()) null else field.fromString(entryIdString)
            val value = entryId?.let { items.firstOrNull { item -> item.first == entryId } }

            field.update(items, value, true)
        }

        itemList.on("blur") {
            itemList.hide()
        }
    }

    override fun onPause() {
        itemList.hide()
    }

    override fun buildFieldValue() {
        with(field) {
            container = div(context.styles.selectContainer) {
                + row(context.styles.selectedOption) {
                    + selectedOption
                    + ZkIcon(ZkIcons.arrowDropDown, size = 24).also { arrow = it }
                    on("click") { toggleItems() }
                }
            }

            container.tabIndex = 0

            on(container, "keydown") {
                it as KeyboardEvent

                // FIXME add keyboard navigation to the list itself

                when (it.key) {
                    "Enter", "ArrowDown" -> {
                        it.preventDefault()
                        if (itemList.isHidden()) {
                            toggleItems()
                        }
                    }
                    "Escape" -> {
                        it.preventDefault()
                        itemList.hide()
                    }
                }
            }

            readOnly = context.readOnly // initialization does not execute setter

            + container
        }
    }

    override fun render(value: VT?) {
        itemList.hide()

        var s = ""

        if (value == null) {
            s += """<div class="${context.styles.selectEntry} ${context.styles.selected}" data-$DATASET_KEY="">${localizedStrings.notSelected}</div>"""
            selectedOption.innerText = localizedStrings.notSelected
            field.selectedItem = null
        } else {
            s += """<div class="${context.styles.selectEntry}" data-$DATASET_KEY="">${localizedStrings.notSelected}</div>"""
        }

        items.forEach {
            if (it.first == value) {
                s += """<div class=" ${context.styles.selectEntry} ${context.styles.selected}" data-$DATASET_KEY="${it.first}">${escape(it.second)}</div>"""
                selectedOption.innerText = it.second
                field.selectedItem = it
            } else {
                s += """<div class="${context.styles.selectEntry}" data-$DATASET_KEY="${it.first}">${escape(it.second)}</div>"""
            }
        }

        itemList.innerHTML = s
    }

    override fun focusValue() = toggleItems()

    open fun toggleItems() {
        if (field.readOnly) return

        itemList.element.style.height = "auto"
        itemList.element.style.maxHeight = "auto"

        itemList.toggle(selectedOption.element, context.styles.fieldHeight * 5)
    }

}