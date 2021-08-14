/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.table.columns

import zakadabar.core.data.BaseBo
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.table.ZkTable
import zakadabar.core.resource.localizedStrings
import kotlin.reflect.KProperty1

open class ZkOptLongColumn<T : BaseBo>(
    table: ZkTable<T>,
    private val prop: KProperty1<T, Long?>
) : ZkColumn<T>(table) {

    override fun onCreate() {
        label = localizedStrings.getNormalized(prop.name)
        super.onCreate()
    }

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            + format(row)
        }
    }

    override fun sort() {
        table.fullData = if (sortAscending) {
            table.fullData.sortedBy { format(it.data) }
        } else {
            table.fullData.sortedByDescending { format(it.data) }
        }
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return (string in format(row).lowercase())
    }

    override fun exportCsv(row: T): String {
        return format(row)
    }

    open fun format(row: T) = prop.get(row)?.toString() ?: ""

}