/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.field.select

import org.w3c.dom.HTMLElement
import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.get
import org.w3c.dom.set
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.field.*
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.browser.layout.ZkFullScreenLayout.zke
import zakadabar.core.browser.popup.ZkPopUp
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.localizedStrings

open class DropdownRenderer<VT, FT : ZkSelectBaseV2<VT, FT>>(
    var itemRenderer: (field: ZkSelectBaseV2<VT, FT>, item: Pair<VT, String>?, selected: Boolean) -> ZkElement = ::stringItemRenderer
) : SelectRenderer<VT, FT> {

    companion object {
        private const val DATASET_KEY = "value"

        /**
         * The default select renderer, displays a simple div with a string.
         *
         * @param field     The select field this renderer belongs to.
         * @param item      The item to render.
         * @param selected  When true this is the selected item.
         */
        fun <VT, FT : ZkSelectBaseV2<VT, FT>> stringItemRenderer(field: ZkSelectBaseV2<VT, FT>, item: Pair<VT, String>?, selected: Boolean): ZkElement = zke {

            element.dataset[DATASET_KEY] = item?.first?.toString() ?: ""
            element.tabIndex = - 1

            + field.context.styles.selectEntry

            if (selected) + field.context.styles.selected

            if (item == null) {
                + localizedStrings.notSelected
            } else {
                + item.second
            }
        }

    }

    override lateinit var field: ZkSelectBaseV2<VT, FT>

    open lateinit var container: HTMLElement

    open lateinit var arrow: ZkElement

    open lateinit var itemList: ZkPopUp

    open val selectedOption = ZkElement()

    override fun readOnly(value: Boolean) {
        if (! ::container.isInitialized) return

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

        itemList.on("blur") {
            onBlur(it as FocusEvent) // this one is in effect when there is no filter
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

    /**
     * Called when there is a filter and the user clicks outside it.
     */
    fun onBlur(event: FocusEvent) {
        val target = event.relatedTarget

        if (target !is HTMLElement) {
            itemList.hide()
            return
        }

        onClick(target)
    }

    /**
     * Called by onFilterBlur and itemList.onClick. When the user
     *
     * - clicks outside the select list -> close the popup
     * - clicks on the filter -> give focus to the filter
     * - clicks on an item -> select the item
     */
    fun onClick(target: HTMLElement) {
        val itemIdString = target.dataset[DATASET_KEY] ?: return

        val itemId = if (itemIdString.isEmpty()) null else field.fromString(itemIdString)
        val value = itemId?.let { items.firstOrNull { item -> item.first == itemId } }

        field.update(items, value, true)

        itemList.hide()
    }

    override fun render(value: VT?) {
        itemList.hide()
        itemList.clear()

        if (field.filter) {
            itemList += ZkValueStringField(STANDALONE_NOLABEL, "", { "" }, { }).also {
                it.onBlur { event,_ -> onBlur(event) }
                it.onChange {   }
            }
        }

        if (value == null) {
            itemList += itemRenderer(field, null, true)
            selectedOption.innerText = localizedStrings.notSelected
            field.selectedItem = null
        } else {
            itemList += itemRenderer(field, null, false)
        }

        items.forEach {
            if (it.first == value) {
                itemList += itemRenderer(field, it, true)
                selectedOption.innerText = it.second
                field.selectedItem = it
            } else {
                itemList += itemRenderer(field, it, false)
            }
        }
    }

    override fun focusValue() = toggleItems()

    open fun toggleItems() {
        if (field.readOnly) return

        itemList.element.style.height = "auto"
        itemList.element.style.maxHeight = "auto"

        itemList.toggle(selectedOption.element, context.styles.fieldHeight * 5)

        itemList.firstOrNull<ZkValueStringField>()?.focusValue()
    }

}