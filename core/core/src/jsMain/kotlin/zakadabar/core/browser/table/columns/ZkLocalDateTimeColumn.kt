/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import kotlinx.datetime.LocalDateTime
import zakadabar.core.data.BaseBo
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localized
import zakadabar.core.resource.localizedStrings
import kotlin.reflect.KProperty1

open class ZkLocalDateTimeColumn<T : BaseBo>(
    table: ZkTable<T>,
    private val prop: KProperty1<T, LocalDateTime>
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
            table.fullData.sortedBy { prop.get(it.data) }
        } else {
            table.fullData.sortedByDescending { prop.get(it.data) }
        }
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return (string in format(row).lowercase())
    }

    override fun exportCsv(row: T): String {
        return prop.get(row).localized
    }

    open fun format(row: T): String {
        return prop.get(row).localized
    }
}