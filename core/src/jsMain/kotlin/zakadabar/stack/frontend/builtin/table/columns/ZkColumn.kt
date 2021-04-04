/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.table.ZkTableStyles
import zakadabar.stack.frontend.util.minusAssign
import zakadabar.stack.frontend.util.plusAssign
import kotlin.math.max

open class ZkColumn<T : DtoBase>(
    val table: ZkTable<T>
) : ZkElement(
    element = document.createElement("th") as HTMLElement
) {

    open val min = 24.0
    open val fraction = "1fr"
    open var size = Double.NaN
    open var exportable = true

    lateinit var label: String
    var sortSign = ZkElement() css ZkTableStyles.sortSign

    var beingResized = false

    var sortAscending = false

    override fun onCreate() {
        + label
        + sortSign
        + span(ZkTableStyles.resizeHandle) {
            on(buildElement, "mousedown", ::onResizeMouseDown)
        }
        on("click", ::onClick)
    }

    fun gridTemplate(): String {
        return "minmax(${min}px, $fraction)"
    }

    open fun render(builder: ZkElement, index: Int, row: T) {

    }

    open fun onClick(event: Event) {
        sortAscending = ! sortAscending

        sort()

        table.columns.forEach {
            it.sortSign.hide()
        }

        if (sortAscending) {
            sortSign.classList -= ZkTableStyles.sortedDescending
            sortSign.classList += ZkTableStyles.sortedAscending
        } else {
            sortSign.classList += ZkTableStyles.sortedDescending
            sortSign.classList -= ZkTableStyles.sortedAscending
        }

        sortSign.show()

        table.filter()
        table.render()
    }

    open fun onResizeMouseDown(event: Event) {
        beingResized = true
        classList += ZkTableStyles.beingResized
        table.classList += ZkTableStyles.noSelect

        on(window, "mouseup", mouseUpWrapper)
        on(window, "mousemove", mouseMoveWrapper)
    }

    private val mouseUpWrapper = { event: Event -> onMouseUp(event) }

    open fun onMouseUp(event: Event) {
        beingResized = false
        classList -= ZkTableStyles.beingResized
        table.classList -= ZkTableStyles.noSelect
        window.removeEventListener("mouseup", mouseUpWrapper)
        window.removeEventListener("mousemove", mouseMoveWrapper)
    }

    private val mouseMoveWrapper = { event: Event -> onMouseMove(event) }

    open fun onMouseMove(event: Event) {
        window.requestAnimationFrame {
            if (! beingResized) return@requestAnimationFrame

            event as MouseEvent

            val horizontalScrollOffset = document.documentElement !!.scrollLeft
            size = max(min, (horizontalScrollOffset + event.clientX) - element.offsetLeft)

            val tableWidth = table.tableElement.clientWidth
            var sumWidth = 0.0

            table.columns.forEach {
                if (it.size.isNaN()) {
                    it.size = it.element.clientWidth.toDouble()
                }
                sumWidth += it.size
            }

            val template = if (sumWidth >= tableWidth || table.columns.size == 0) {
                "grid-template-columns: " + table.columns.joinToString(" ") { "${it.size}px" }
            } else {
                "grid-template-columns: " + table.columns.subList(0, table.columns.size - 1).joinToString(" ") { "${it.size}px" } + " 1fr"
            }

            table.tableElement.style.cssText = template
        }
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
