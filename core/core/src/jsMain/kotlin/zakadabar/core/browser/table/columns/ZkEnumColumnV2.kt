/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.localizedStrings

open class ZkEnumColumnV2<T : BaseBo, E : Enum<E>>(
    table: ZkTable<T>,
    val getter : (T) -> E
) : ZkColumn<T>(table) {

    override fun sort() {
        table.sort(sortAscending) { format(it.data) }
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return (string in format(row).lowercase())
    }

    override fun exportCsv(row: T): String {
        return "\"${format(row).replace("\"", "\"\"")}\""
    }

    override fun format(row: T) = localizedStrings.getNormalized(getter(row).name)

    override fun exportRaw(row: T): Any? = getter(row)

}