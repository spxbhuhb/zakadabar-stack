/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import kotlin.reflect.KProperty1

open class ZkInstantColumn<T : DtoBase>(
    override val table: ZkTable<T>,
    private val prop: KProperty1<T, Instant>
) : ZkColumn<T> {

    override var label = Application.stringMap[prop.name] ?: prop.name

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            // FIXME proper formatting, Kotlin datatime supports only ISO for now
            + prop.get(row).toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        }
    }

}