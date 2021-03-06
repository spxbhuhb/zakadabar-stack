/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import zakadabar.stack.data.BaseBo
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.resources.localizedStrings
import zakadabar.stack.util.UUID
import kotlin.reflect.KProperty1

open class ZkOptUuidColumn<T : BaseBo>(
    table: ZkTable<T>,
    private val prop: KProperty1<T, UUID?>
) : ZkColumn<T>(table) {

    override fun onCreate() {
        label = localizedStrings.getNormalized(prop.name)
        super.onCreate()
    }

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            + prop.get(row)?.toString() ?: ""
        }
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
        return (prop.get(row)?.toString()?.contains(string)) ?: false
    }

    override fun exportCsv(row: T): String {
        return prop.get(row)?.toString() ?: ""
    }

}