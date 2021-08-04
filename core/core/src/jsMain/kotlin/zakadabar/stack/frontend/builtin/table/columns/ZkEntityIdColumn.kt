/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.resources.css.em
import zakadabar.stack.resources.localizedStrings
import kotlin.reflect.KProperty1

open class ZkEntityIdColumn<T : BaseBo, IT>(
    table: ZkTable<T>,
    private val prop: KProperty1<T, EntityId<IT>>
) : ZkColumn<T>(table) {

    override var max = 8.em

    override fun onCreate() {
        label = localizedStrings.getNormalized(prop.name)
        super.onCreate()
    }

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            + "# ${prop.get(row)}"
        }
    }

    infix fun build(builder: ZkEntityIdColumn<T, IT>.() -> Unit): ZkEntityIdColumn<T, IT> {
        this.builder()
        return this
    }

    override fun sort() {
        table.fullData = if (sortAscending) {
            table.fullData.sortedBy { prop.get(it.data) }
        } else {
            table.fullData.sortedByDescending { prop.get(it.data) }
        }
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return (string in prop.get(row).toString().lowercase())
    }

    override fun exportCsv(row: T): String {
        return prop.get(row).toString()
    }

}