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
import zakadabar.core.resource.css.px
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

    open val selectedOption = ZkElement()

    /**
     * List of options and an filter (when field.filter is true).
     */
    open lateinit var popup: ZkPopUp

    /**
     * Filter to let the user search between options. List of options
     * are updated real time as the user types into the filter.
     */
    open val filter: ZkFieldBase<String, *> =
        ZkValueStringField(STANDALONE_NOLABEL, "", { "" }).apply {
            + context.styles.selectOptionFilter
            this.onFocusOut { event, _ -> onFocusOut(event) }
            this.onChange { filterValue -> onFilterChange(filterValue) }
        }

    /**
     * List of the options to show when the user clicks on the down arrow.
     */
    open val list = ZkElement()

    /**
     * List of items to display.
     *
     * When filtering is enabled this list contains the filtered items only.
     * When filtering is disabled this list contains all items.
     */
    open var filteredItems: List<Pair<VT, String>>? = null

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
        popup = ZkPopUp {
            + context.styles.selectOptionPopup

            element.tabIndex = 0

            on("focusout") {
                onFocusOut(it as FocusEvent) // this one is in effect when there is no filter
            }
        }

        list.apply { + context.styles.selectOptionList }
    }

    override fun onPause() {
        popup.hide()
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
                        if (popup.isHidden()) {
                            toggleItems()
                        }
                    }
                    "Escape" -> {
                        it.preventDefault()
                        popup.hide()
                    }
                }
            }

            readOnly = context.readOnly // initialization does not execute setter

            + container
        }
    }

    /**
     * Called when the filter or the select looses focus. The relatedTarget is
     * the element that looses the focus. When it is inside the popup, try
     * to interpret it as a click. When it is outside the popup, hide the popup.
     */
    fun onFocusOut(event: FocusEvent) {
        val relatedTarget = event.relatedTarget

        if (relatedTarget is HTMLElement && popup.element.contains(relatedTarget)) {
            onClick(relatedTarget)
        } else {
            popup.hide()
        }
    }

    /**
     * Called by onFocusOut and itemList.onClick. When the user
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

        popup.hide()
    }

    override fun render(value: VT?, hide: Boolean) {
        if (hide) popup.hide()

        if (field.filter && filter !in popup.childElements) popup += filter
        if (list !in popup.childElements) popup += list

        list.clear()

        if (value == null) {
            list += itemRenderer(field, null, true)
            selectedOption.innerText = localizedStrings.notSelected
            field.selectedItem = null
        } else {
            list += itemRenderer(field, null, false)
        }

        (filteredItems ?: items).forEach {
            if (it.first == value) {
                list += itemRenderer(field, it, true)
                selectedOption.innerText = it.second
                field.selectedItem = it
            } else {
                list += itemRenderer(field, it, false)
            }
        }

        popup.setStyles()
        popup.align(selectedOption.element, context.styles.fieldHeight * 5)
    }

    override fun focusValue() = toggleItems()

    open fun onFilterChange(filterValue: String) {
        val lowercaseFilterValue = filterValue.lowercase()

        filteredItems = if (lowercaseFilterValue.isEmpty()) {
            items
        } else {
            items.filter { lowercaseFilterValue in it.second.lowercase() }
        }

        render(field.selectedItem?.first, false)

        focus()
    }

    open fun toggleItems() {
        if (field.readOnly) return

        popup.setStyles()
        popup.toggle(selectedOption.element, context.styles.fieldHeight * 5)

        focus()
    }

    fun ZkPopUp.setStyles() {
        element.style.height = "auto"
        element.style.maxHeight = "auto"
        element.style.width = (container.getBoundingClientRect().width - context.styles.indent * 2).px
    }

    open fun focus() {
        if (field.filter) {
            filter.focusValue()
        } else {
            popup.focus()
        }
    }

}