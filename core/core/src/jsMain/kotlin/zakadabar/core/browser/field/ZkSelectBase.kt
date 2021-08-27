/*
 * Copyright © 2020, Simplexion, Hungary and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.core.browser.field

import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.browser.popup.ZkPopUp
import zakadabar.core.browser.util.escape
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.localizedStrings

abstract class ZkSelectBase<VT, FT : ZkSelectBase<VT, FT>>(
    context: ZkFieldContext,
    propName: String,
    var onSelectCallback: (Pair<VT, String>?) -> Unit = { }
) : ZkFieldBase<VT, FT>(
    context = context,
    propName = propName
) {

    companion object {
        private const val DATASET_KEY = "value"
    }

    var sort = true

    var fetch: (suspend () -> List<Pair<VT, String>>)? = null
        set(value) {
            field = value
            fetchAndRender()
        }

    open lateinit var container: HTMLElement

    open lateinit var arrow: ZkElement

    open val selectedOption = ZkElement()

    open val itemList = ZkPopUp { + context.styles.selectOptionList }

    lateinit var items: List<Pair<VT, String>>

    var selectedItem: Pair<VT, String>? = null

    override var readOnly = context.readOnly
        set(value) {
            if (value) {
                container.firstElementChild?.classList?.plusAssign(context.styles.disabledSelect)
                arrow.hide()
            } else {
                container.firstElementChild?.classList?.minusAssign(context.styles.disabledSelect)
                arrow.show()
            }
            field = value
        }

    override var valueOrNull: VT?
        get() = selectedItem?.first
        set(value) {
            val item = value?.let { v -> items.firstOrNull { it.first == v } }
            setPropValue(item)
            render(item?.first)
        }

    override fun onPause() {
        super.onPause()
        itemList.hide()
    }

    open suspend fun getItems(): List<Pair<VT, String>> {
        return fetch?.invoke() ?: throw NotImplementedError()
    }

    open fun sortItems() {
        if (sort) items = items.sortedBy { it.second }
    }

    abstract fun fromString(string: String): VT

    abstract fun getPropValue(): VT?

    abstract fun setPropValue(value: Pair<VT, String>?)

    open fun update(items: List<Pair<VT, String>>, value: Pair<VT, String>?) {
        this.items = items
        setPropValue(value)
        onSelectCallback(value)
        render(value?.first) // FIXME this re-rendering is a bit too expensive I think
        itemList.hide()
    }

    override fun buildFieldValue() {
        itemList.on("click") {
            it as MouseEvent

            val target = it.target
            if (target !is HTMLElement) return@on

            val entryIdString = target.dataset[DATASET_KEY]
            val entryId = if (entryIdString.isNullOrEmpty()) null else fromString(entryIdString)
            val value = entryId?.let { items.firstOrNull { item -> item.first == entryId } }

            update(this.items, value)
        }

        container = div(context.styles.selectContainer) {
            + row(context.styles.selectedOption) {
                + selectedOption
                + ZkIcon(ZkIcons.arrowDropDown, size = 24).also { arrow = it }
                on("click") { toggleItems() }
            }
        }

        container.tabIndex = 0
        itemList.element.tabIndex = 0

        itemList.on("blur") {
            itemList.hide()
        }

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

        if (readOnly) arrow.hide()

        + container
    }

    open fun fetchAndRender() {
        io {
            items = getItems()
            render(getPropValue())
        }
    }

    open fun render(value: VT?) {
        var s = ""

        if (value == null) {
            s += """<div class="${context.styles.selectEntry} ${context.styles.selected}" data-${DATASET_KEY}="">${localizedStrings.notSelected}</div>"""
            selectedOption.innerText = localizedStrings.notSelected
            selectedItem = null
        } else {
            s += """<div class="${context.styles.selectEntry}" data-${DATASET_KEY}="">${localizedStrings.notSelected}</div>"""
        }

        items.forEach {
            if (it.first == value) {
                s += """<div class=" ${context.styles.selectEntry} ${context.styles.selected}" data-${DATASET_KEY}="${it.first}">${escape(it.second)}</div>"""
                selectedOption.innerText = it.second
                selectedItem = it
            } else {
                s += """<div class="${context.styles.selectEntry}" data-${DATASET_KEY}="${it.first}">${escape(it.second)}</div>"""
            }
        }

        itemList.innerHTML = s
    }

    override fun focusValue() = toggleItems()

    open fun toggleItems() {
        if (readOnly) return

        itemList.element.style.height = "auto"
        itemList.element.style.maxHeight = "auto"

        itemList.toggle(selectedOption.element, context.styles.fieldHeight * 5)
    }

}