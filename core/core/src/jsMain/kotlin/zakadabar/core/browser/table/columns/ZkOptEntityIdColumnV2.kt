/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId

open class ZkOptEntityIdColumnV2<T : BaseBo, IT>(
    table: ZkTable<T>,
    val getter : (T) -> EntityId<IT>?
) : ZkColumn<T>(table) {

    override fun render(cell: ZkElement, index: Int, row: T) {
        with(cell) {
            getter(row)?.let {
                + "# $it"
            }
        }
    }

    infix fun build(builder: ZkOptEntityIdColumnV2<T, IT>.() -> Unit): ZkOptEntityIdColumnV2<T, IT> {
        this.builder()
        return this
    }

    override fun sort() {
        table.sort(sortAscending) { getter(it.data) }
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return getter(row)?.let {
            string in it.value.lowercase()
        } ?: false
    }

    override fun exportCsv(row: T): String =
        getter(row)?.value ?: ""

}