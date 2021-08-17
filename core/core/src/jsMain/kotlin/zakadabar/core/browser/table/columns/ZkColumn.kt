/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import zakadabar.core.data.BaseBo
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.table.zkTableStyles
import zakadabar.core.resource.css.px
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
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

    lateinit var label: String
    var sortSign = ZkElement() css zkTableStyles.sortSign

    var beingResized = false

    var sortAscending = false

    override fun onCreate() {
        + label
        + sortSign
        + span(zkTableStyles.resizeHandle) {
            on(buildPoint, "mousedown", ::onResizeMouseDown)
        }
        on("click", ::onClick)
    }

    fun gridTemplate(): String {
        return "minmax(${min.px}, $max)"
    }

    open fun render(builder: ZkElement, index: Int, row: T) {

    }

    open fun onClick(event: Event) {
        sortAscending = ! sortAscending

        if (! table::fullData.isInitialized) return

        sort()

        table.columns.forEach {
            it.sortSign.hide()
        }

        if (sortAscending) {
            sortSign.classList -= zkTableStyles.sortedDescending
            sortSign.classList += zkTableStyles.sortedAscending
        } else {
            sortSign.classList += zkTableStyles.sortedDescending
            sortSign.classList -= zkTableStyles.sortedAscending
        }

        sortSign.show()

        table.filter()
        table.render()
    }

    open fun onResizeMouseDown(event: Event) {
        beingResized = true
        classList += zkTableStyles.beingResized
        table.classList += zkTableStyles.noSelect

        on(window, "mouseup", mouseUpWrapper)
        on(window, "mousemove", mouseMoveWrapper)
    }

    private val mouseUpWrapper = { event: Event -> onMouseUp(event) }

    open fun onMouseUp(event: Event) {
        beingResized = false
        classList -= zkTableStyles.beingResized
        table.classList -= zkTableStyles.noSelect
        window.removeEventListener("mouseup", mouseUpWrapper)
        window.removeEventListener("mousemove", mouseMoveWrapper)
    }

    private val mouseMoveWrapper = { event: Event -> onMouseMove(event) }

    open fun onMouseMove(event: Event) {
        window.requestAnimationFrame {
            if (! beingResized) return@requestAnimationFrame

            event as MouseEvent

            val horizontalScrollOffset = document.documentElement !!.scrollLeft
            val parentOffset = findParentOffset()
            val elementOffset = element.offsetLeft

            size = max(min, horizontalScrollOffset + event.clientX - parentOffset - elementOffset)

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

    private fun findParentOffset(): Int {
        var current = element.offsetParent as? HTMLElement
        while (current != null) {
            if (current.offsetLeft != 0) return current.offsetLeft
            current = current.offsetParent as? HTMLElement
        }
        return 0
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
