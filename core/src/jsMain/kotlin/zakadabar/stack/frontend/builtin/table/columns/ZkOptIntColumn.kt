/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import kotlin.reflect.KProperty1

open class ZkOptIntColumn<T : DtoBase>(
    table: ZkTable<T>,
    private val prop: KProperty1<T, Int?>
) : ZkColumn<T>(table) {

    override fun onCreate() {
        label = stringStore.getNormalized(prop.name)
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
        return (string in format(row))
    }

    override fun exportCsv(row: T): String {
        return format(row)
    }

    open fun format(row: T) = prop.get(row)?.toString() ?: ""

}