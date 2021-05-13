/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import kotlin.reflect.KProperty1

open class ZkRecordIdColumn<T : DtoBase, IT>(
    table: ZkTable<T>,
    private val prop: KProperty1<T, RecordId<IT>>
) : ZkColumn<T>(table) {

    override fun onCreate() {
        label = application.strings.map[prop.name] ?: prop.name
        super.onCreate()
    }

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            + "# ${prop.get(row)}"
        }
    }

    infix fun build(builder: ZkRecordIdColumn<T, IT>.() -> Unit): ZkRecordIdColumn<T, IT> {
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
        return (string in prop.get(row).toString())
    }

    override fun exportCsv(row: T): String {
        return prop.get(row).toString()
    }

}