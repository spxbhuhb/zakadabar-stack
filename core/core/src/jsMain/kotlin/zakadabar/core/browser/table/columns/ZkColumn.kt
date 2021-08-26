/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.css.px
import kotlin.math.max

open class ZkColumn<T : BaseBo>(
    val table: ZkTable<T>
) : ZkElement(
    element = document.createElement("th") as HTMLElement
) {

    open val min = 24.0
    open var max = "1fr"

    @Deprecated("EOL: 2021.8.1  -  use max instead", ReplaceWith("max"))
    open var fraction by ::max
    open var size = Double.NaN
    open var exportable = true

    open var label: String = ""
    var sortSign = ZkElement() css table.styles.sortSign

    var beingResized = false
    var beenResized = false
    var lastX: Int = 0

    var sortAscending = false

    override fun onCreate() {
        + label
        + sortSign
        + span(table.styles.resizeHandle) {
            on(buildPoint, "mousedown", ::onResizeMouseDown)
        }
        on("click", ::onClick)
    }

    /**
     * [ZkTable] calls this method whenever [ZkTable.setData] runs.
     */
    open fun onTableSetData() {

    }

    fun gridTemplate(): String {
        return "minmax(${min.px}, $max)"
    }

    open fun render(cell: ZkElement, index: Int, row: T) {

    }

    open fun onClick(event: Event) {
        if (beenResized) {
            beenResized = false
            return
        }

        sortAscending = ! sortAscending

        if (! table::fullData.isInitialized) return

        sort()

        table.columns.forEach {
            it.sortSign.hide()
        }

        if (sortAscending) {
            sortSign.classList -= table.styles.sortedDescending
            sortSign.classList += table.styles.sortedAscending
        } else {
            sortSign.classList += table.styles.sortedDescending
            sortSign.classList -= table.styles.sortedAscending
        }

        sortSign.show()

        table.filter()
        table.render()
    }

    open fun onResizeMouseDown(event: Event) {
        event as MouseEvent

        beingResized = true
        beenResized = true
        lastX = event.clientX

        table.columns.forEach {
            if (it.id != id) it.classList += table.styles.otherBeingResized
        }

        classList += table.styles.beingResized
        table.classList += table.styles.noSelect

        on(window, "mouseup", mouseUpWrapper)
        on(window, "mousemove", mouseMoveWrapper)
    }

    private val mouseUpWrapper = { event: Event -> onMouseUp(event) }

    open fun onMouseUp(event: Event) {
        beingResized = false
        lastX = 0

        table.columns.forEach {
            it.classList -= table.styles.otherBeingResized
        }

        classList -= table.styles.beingResized
        table.classList -= table.styles.noSelect

        window.removeEventListener("mouseup", mouseUpWrapper)
        window.removeEventListener("mousemove", mouseMoveWrapper)

        event.preventDefault()
    }

    private val mouseMoveWrapper = { event: Event -> onMouseMove(event) }

    open fun onMouseMove(event: Event) {
        window.requestAnimationFrame {
            if (! beingResized) return@requestAnimationFrame

            event as MouseEvent

            val distance = event.clientX - lastX
            lastX = event.clientX

            size = max(min, if (size.isNaN()) element.clientWidth.toDouble() else size + distance)

            val tableWidth = table.tableElement.clientWidth
            var sumWidth = 0.0

            table.columns.forEach {
                if (it.size.isNaN()) {
                    it.size = it.element.clientWidth.toDouble()
                }
                sumWidth += it.size
            }

            // When the table is smaller than the sum of colum widths, use 1 fraction for the
            // last column. When larger, use exact pixel widths for each column.

            val template = if (sumWidth >= tableWidth || table.columns.size == 0) {
                table.columns.joinToString(" ") { "${it.size}px" }
            } else {
                table.columns.subList(0, table.columns.size - 1).joinToString(" ") { "${it.size}px" } + " 1fr"
            }

            table.tableElement.width = sumWidth.px
            table.tableElement.style.setProperty("grid-template-columns", template)
        }
    }

    private fun findParentOffset(): Int {
        var offset = 0
        var current = element.offsetParent as? HTMLElement
        while (current != null) {
            if (current.offsetLeft != 0) offset += current.offsetLeft
            current = current.offsetParent as? HTMLElement
        }
        return offset
    }

    /**
     * Sorts the table data by this column.
     */
    open fun sort() {

    }

    /**
     * Checks if this column of the given row matches the given string or not.
     */
    open fun matches(row: T, string: String?): Boolean {
        return false
    }

    open fun exportCsv(row: T): String {
        return ""
    }

}
