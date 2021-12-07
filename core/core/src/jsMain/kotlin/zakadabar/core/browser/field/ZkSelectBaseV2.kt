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

import zakadabar.core.browser.field.select.SelectRenderer
import zakadabar.core.browser.util.io

abstract class ZkSelectBaseV2<VT, FT : ZkSelectBaseV2<VT, FT>>(
    context: ZkFieldContext,
    label: String,
    val renderer : SelectRenderer<VT, FT>,
    var onSelectCallback: (Pair<VT, String>?) -> Unit = { },
    getter: () -> VT?,
    setter: (VT?) -> Unit = {}
) : ZkFieldBase<VT, FT>(
    context = context,
    propName = label
) {

    open var getter = getter

    var sort = true

    var fetch: (suspend () -> List<Pair<VT, String>>)? = null
        set(value) {
            field = value
            fetchAndRender()
        }

    lateinit var items: List<Pair<VT, String>>

    var selectedItem: Pair<VT, String>? = null

    override var readOnly = context.readOnly
        set(value) {
            renderer.readOnly(value)
            field = value
        }

    override var valueOrNull: VT?
        get() = selectedItem?.first
        set(value) {
            val item = value?.let { v -> items.firstOrNull { it.first == v } }
            selectedItem = item
            setBackingValue(item, false)
            renderer.render(item?.first)
        }

    override fun onCreate() {
        super.onCreate()
        renderer.field = this
        renderer.onCreate()
    }

    override fun onPause() {
        super.onPause()
        renderer.onPause()
    }

    open suspend fun getItems(): List<Pair<VT, String>> {
        return fetch?.invoke() ?: throw NotImplementedError()
    }

    open fun sortItems() {
        if (sort) items = items.sortedBy { it.second }
    }

    abstract fun fromString(string: String): VT

    abstract fun setBackingValue(value: Pair<VT, String>?, user : Boolean)

    open fun update(items: List<Pair<VT, String>>, value: Pair<VT, String>?, user : Boolean) {
        this.items = items
        setBackingValue(value, user)
        onSelectCallback(value)
        renderer.render(value?.first) // FIXME this re-rendering is a bit too expensive I think
    }

    override fun buildFieldValue() {
        // this is postponed until the first render
        renderer.field = this
        renderer.buildFieldValue()
    }

    open fun fetchAndRender() {
        io {
            items = getItems()
            renderer.render(getter())
        }
    }

    override fun focusValue() = renderer.focusValue()

}