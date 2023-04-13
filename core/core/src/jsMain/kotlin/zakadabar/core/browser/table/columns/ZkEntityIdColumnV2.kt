/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.resource.css.em

open class ZkEntityIdColumnV2<T : BaseBo, IT>(
    table: ZkTable<T>,
    val getter: (T) -> EntityId<IT>
) : ZkColumn<T>(table) {

    override var max = 8.em

    infix fun build(builder: ZkEntityIdColumnV2<T, IT>.() -> Unit): ZkEntityIdColumnV2<T, IT> {
        this.builder()
        return this
    }

    override fun sort() {
        table.sort(sortAscending) { getter(it.data) }
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return (string in getter(row).toString().lowercase())
    }

    override fun exportCsv(row: T): String {
        return getter(row).toString()
    }

    override fun exportRaw(row: T): Any? = getter(row)

    override fun format(row: T): String {
        return "# ${getter(row)}"
    }
}