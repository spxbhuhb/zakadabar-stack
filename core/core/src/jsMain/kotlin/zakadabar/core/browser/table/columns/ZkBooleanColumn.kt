/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.em
import zakadabar.core.resource.localizedStrings
import kotlin.reflect.KProperty1

open class ZkBooleanColumn<T : BaseBo>(
    table: ZkTable<T>,
    private val prop: KProperty1<T, Boolean>
) : ZkColumn<T>(table) {

    override var max = 3.em

    override fun onCreate() {
        label = localizedStrings.getNormalized(prop.name)
        super.onCreate()
    }

    override fun render(cell: ZkElement, index: Int, row: T) {
//        val checkbox = document.createElement("input") as HTMLInputElement
//        checkbox.type = "checkbox"
//        checkbox.value = prop.get(row).toString()
//        checkbox.checked = prop.get(row)
//        checkbox.style.cssText = "pointer-events:none"
        with(cell) {
            + div {
                buildPoint.innerHTML = ZkIcons.check.svg(18)
            }
        }
    }

    override fun sort() {
        table.fullData = if (sortAscending) {
            table.fullData.sortedBy { prop.get(it.data) }
        } else {
            table.fullData.sortedByDescending { prop.get(it.data) }
        }
    }

}