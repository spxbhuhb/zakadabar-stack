/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.localizedStrings

open class ZkActionsColumn<T : BaseBo>(
    table: ZkTable<T>,
    val builder: ZkActionsColumn<T>.() -> Unit
) : ZkColumn<T>(table) {

    class Action<T : BaseBo>(
        val label: String,
        val callback: (Int, T) -> Unit
    )

    val actions = mutableListOf<Action<T>>()

    override fun onCreate() {
        label = localizedStrings.actions
        exportable = false

        super.onCreate()

        builder.invoke(this)
    }

    override fun render(cell: ZkElement, index: Int, row: T) {
        with(cell) {
            + table.styles.actions

            actions.forEach { action ->
                + div(table.styles.actionEntry) {
                    + action.label
                    on(buildPoint, "click") { action.callback(index, row) }
                }
            }
        }
    }

    open fun action(label: String, callback: (Int, T) -> Unit) =
        Action(label, callback)

    operator fun Action<T>.unaryPlus() {
        actions += this
    }


}