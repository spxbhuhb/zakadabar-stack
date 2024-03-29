/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.em
import zakadabar.core.resource.localizedStrings

open class ZkOptBooleanColumnV2<T : BaseBo>(
    table: ZkTable<T>,
    val getter: (T) -> Boolean?
) : ZkColumn<T>(table) {

    override var max = 3.em

    override fun render(cell: ZkElement, index: Int, row: T) {
        with(cell) {
            + div {
                buildPoint.innerHTML = when(getter(row)) {
                    true -> ZkIcons.check.svg(18)
                    false ->ZkIcons.close.svg(18)
                    else -> ""
                }
            }
        }
    }

    override fun sort() {
        table.sort(sortAscending) { getter(it.data) }
    }

    override fun exportCsv(row: T): String =
        when (getter(row)) {
            null -> ""
            true -> localizedStrings.trueText
            false -> localizedStrings.falseText
        }

    override fun exportRaw(row: T): Any? = getter(row)
}