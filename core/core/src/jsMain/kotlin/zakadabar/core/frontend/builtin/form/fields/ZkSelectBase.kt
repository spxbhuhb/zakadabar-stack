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
package zakadabar.core.frontend.builtin.form.fields

import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.get
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.form.ZkFormStyles
import zakadabar.core.frontend.builtin.form.zkFormStyles
import zakadabar.core.frontend.builtin.icon.ZkIcon
import zakadabar.core.frontend.builtin.popup.ZkPopUp
import zakadabar.core.frontend.resources.ZkIcons
import zakadabar.core.frontend.util.escape
import zakadabar.core.frontend.util.io
import zakadabar.core.frontend.util.minusAssign
import zakadabar.core.frontend.util.plusAssign
import zakadabar.core.resource.localizedStrings

abstract class ZkSelectBase<VT>(
    context: ZkFieldContext,
    propName: String,
    var onSelected: (Pair<VT, String>?) -> Unit = { }
) : ZkFieldBase<VT>(
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

    open val itemList = ZkPopUp { + ZkFormStyles.selectOptionList }

    lateinit var items: List<Pair<VT, String>>

    override var readOnly = context.readOnly
        set(value) {
            if (value) {
                container.firstElementChild?.classList?.plusAssign(zkFormStyles.disabledSelect)
                arrow.hide()
            } else {
                container.firstElementChild?.classList?.minusAssign(zkFormStyles.disabledSelect)
                arrow.show()
            }
            field = value
        }

    override fun onPause() {
        super.onPause()
        if (itemList.isShown()) itemList.hide()
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

    override fun buildFieldValue() {
        itemList.on("click") {
            it as MouseEvent

            val target = it.target
            if (target !is HTMLElement) return@on

            val entryIdString = target.dataset[DATASET_KEY]
            val entryId = if (entryIdString.isNullOrEmpty()) null else fromString(entryIdString)
            val value = entryId?.let { items.firstOrNull { item -> item.first == entryId } }

            touched = true
            setPropValue(value)
            onSelected(value)
            render(value?.first) // FIXME this re-rendering is a bit too expensive I think
            itemList.hide()
        }

        container = div(ZkFormStyles.selectContainer) {
            + row(ZkFormStyles.selectedOption) {
                + selectedOption
                + ZkIcon(ZkIcons.arrowDropDown, size = 24).also { arrow = it }
                on("click") { toggleOptions() }
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
                        itemList.show()
                    }
                }
                "Escape" -> {
                    it.preventDefault()
                    itemList.hide()
                }
            }
        }

        if (readOnly) arrow.hide()

        itemList.hide()
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
            s += """<div class="${ZkFormStyles.selectEntry} ${ZkFormStyles.selected}" data-${DATASET_KEY}="">${localizedStrings.notSelected}</div>"""
            selectedOption.innerText = localizedStrings.notSelected
        } else {
            s += """<div class="${ZkFormStyles.selectEntry}" data-${DATASET_KEY}="">${localizedStrings.notSelected}</div>"""
        }

        items.forEach {
            if (it.first == value) {
                s += """<div class=" ${ZkFormStyles.selectEntry} ${ZkFormStyles.selected}" data-${DATASET_KEY}="${it.first}">${escape(it.second)}</div>"""
                selectedOption.innerText = it.second
            } else {
                s += """<div class="${ZkFormStyles.selectEntry}" data-${DATASET_KEY}="${it.first}">${escape(it.second)}</div>"""
            }
        }

        itemList.innerHTML = s
    }

    override fun focusValue() = toggleOptions()

    open fun toggleOptions() {
        if (readOnly) return

        itemList.toggle(selectedOption.element, zkFormStyles.rowHeight * 5)

        if (itemList.isShown()) {
            itemList.focus()
        }
    }

}