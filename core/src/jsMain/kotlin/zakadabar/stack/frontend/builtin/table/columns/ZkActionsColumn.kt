/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import org.w3c.dom.set
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.table.ZkTableStyles
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.CoreStrings
import kotlin.reflect.KProperty1

open class ZkActionsColumn<T : RecordDto<T>>(
    override val table: ZkTable<T>,
    private val prop: KProperty1<T, RecordId<T>>,
    private val crud: ZkCrud<T>
) : ZkColumn<T> {

    override var label = Application.stringMap["actions"] ?: prop.name

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            currentElement.classList += ZkTableStyles.action
            currentElement.dataset["action"] = "update"
            + CoreStrings.details
        }
    }

}