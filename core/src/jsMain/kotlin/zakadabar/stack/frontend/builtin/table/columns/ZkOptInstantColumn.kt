/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import kotlin.reflect.KProperty1

open class ZkOptInstantColumn<T : DtoBase>(
    table: ZkTable<T>,
    private val prop: KProperty1<T, Instant?>
) : ZkColumn<T>(table) {

    override fun onCreate() {
        label = ZkApplication.strings.map[prop.name] ?: prop.name
        super.onCreate()
    }

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            + format(row)
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
        return (string in format(row))
    }

    override fun exportCsv(row: T): String {
        return format(row)
    }

    open fun format(row: T): String {
        // FIXME proper formatting, Kotlin datetime supports only ISO for now
        val value = prop.get(row) ?: return ""
        val s = value.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        return "${s.substring(0, 10)} ${s.substring(11, 16)}"
    }
}