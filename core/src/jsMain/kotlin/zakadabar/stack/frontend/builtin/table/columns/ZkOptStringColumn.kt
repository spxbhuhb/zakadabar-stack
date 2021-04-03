/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import kotlin.reflect.KProperty1

open class ZkOptStringColumn<T : DtoBase>(
    table: ZkTable<T>,
    private val prop: KProperty1<T, String?>
) : ZkColumn<T>(table) {

    override fun onCreate() {
        label = ZkApplication.strings.map[prop.name] ?: prop.name
        super.onCreate()
    }

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            + prop.get(row)
        }
    }

    override fun sort() {
        table.fullData = if (sortAscending) {
            table.fullData.sortedBy { prop.get(it) }
        } else {
            table.fullData.sortedByDescending { prop.get(it) }
        }
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return (prop.get(row)?.contains(string)) ?: false
    }

}