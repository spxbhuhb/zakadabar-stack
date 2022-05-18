/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import org.w3c.dom.set
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkRowLevelState
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.ZkIconSource
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.em

open class ZkLevelColumn<T : BaseBo>(
    table: ZkTable<T>,
    val openIcon: ZkIconSource = ZkIcons.arrowRight,
    val closeIcon: ZkIconSource = ZkIcons.arrowDropDown
) : ZkColumn<T>(table) {

    override var max = 2.7.em

    override var exportable: Boolean = false

    override fun render(cell: ZkElement, index: Int, row: T) {
        val rowState = table.renderData[index]
        val levelState = rowState.levelState

        with(cell) {
            + zke(table.styles.multiLevelContainer) {

                when (levelState) {
                    ZkRowLevelState.Single -> null
                    ZkRowLevelState.Open -> closeIcon.svg(24)
                    ZkRowLevelState.Closed -> openIcon.svg(24)
                }?.let { icon ->
                    buildPoint.innerHTML = icon
                    buildPoint.dataset[table.LEVEL_TOGGLE] = "true"
                }

                on("click") {
                    table.toggleMultiLevelRow(rowState)
                }
            }

            if (rowState.level != 0) {
                + table.styles.multiLevelSingle
            }

            if (rowState.level == 0) {
                if (levelState == ZkRowLevelState.Open) {
                    + table.styles.multiLevelOpen
                } else {
                    + table.styles.multiLevelClosed
                }
            }
        }
    }

    override fun sort() {
        // FIXME level column sort
    }

}