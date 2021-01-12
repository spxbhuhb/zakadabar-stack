/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import org.w3c.dom.set
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement
import kotlin.reflect.KProperty1

open class ZkActionsColumn<T>(
    override val table: ZkTable<T>,
    private val prop: KProperty1<T, RecordId<T>>,
    private val crud: ZkCrud<T>
) : ZkColumn<T> {

    override var label = prop.name

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            buildContext.dataset["recordId"] = prop.get(row).toString()
            + "edit"
            on("click") { event ->
                val target = event.target as? HTMLElement ?: return@on
                val recordId = target.dataset["recordId"]?.toLongOrNull() ?: return@on
                crud.openUpdate(recordId)
            }
        }
    }

}