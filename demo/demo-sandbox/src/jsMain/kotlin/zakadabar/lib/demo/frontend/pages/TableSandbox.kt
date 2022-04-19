/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend.pages

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.page.ZkPage
import zakadabar.core.browser.table.ZkTableStyles
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.px
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

var styles by cssStyleSheet(SandboxStyles())

open class SandboxStyles : ZkTableStyles() {

}

//[Log] scrollTop: 12951 scrollLeft: 0
//[Log] scrollTop: 12874 scrollLeft: 0
//[Log] scrollTop: 12798 scrollLeft: 0
//[Log] scrollTop: 12605 scrollLeft: 0
//[Log] scrollTop: 11837 scrollLeft: 0
//[Log] scrollTop: 10453 scrollLeft: 0
//[Log] scrollTop: 9800 scrollLeft: 0
//[Log] scrollTop: 9339 scrollLeft: 0
//[Log] scrollTop: 9070 scrollLeft: 0
//[Log] scrollTop: 8878 scrollLeft: 0
//[Log] scrollTop: 8801 scrollLeft: 0
//[Log] scrollTop: 8762 scrollLeft: 0

//[Log] scrollTop: 8647 scrollLeft: 0
//[Log] scrollTop: 8570 scrollLeft: 0
//[Log] scrollTop: 8455 scrollLeft: 0
//[Log] scrollTop: 8032 scrollLeft: 0
//[Log] scrollTop: 7725 scrollLeft: 0
//[Log] scrollTop: 7417 scrollLeft: 0
//[Log] scrollTop: 6725 scrollLeft: 0
//[Log] scrollTop: 6264 scrollLeft: 0
//[Log] scrollTop: 5765 scrollLeft: 0
//[Log] scrollTop: 5111 scrollLeft: 0
//[Log] scrollTop: 4881 scrollLeft: 0
//[Log] scrollTop: 4727 scrollLeft: 0
//[Log] scrollTop: 4612 scrollLeft: 0
//[Log] scrollTop: 4496 scrollLeft: 0
//[Log] scrollTop: 4381 scrollLeft: 0
//[Log] scrollTop: 4266 scrollLeft: 0
//[Log] scrollTop: 4189 scrollLeft: 0
//[Log] scrollTop: 4151 scrollLeft: 0
//[Log] scrollTop: 4074 scrollLeft: 0
//[Log] scrollTop: 4035 scrollLeft: 0

class TableSandbox<T> : ZkPage() {

    val data = (1..500).map { it - 1 }

    val contentContainer = ZkElement()
    lateinit var tableElement: HTMLTableElement
    lateinit var tableBodyElement: HTMLTableSectionElement

    var estimatedRowHeight = 50.0
    var estimatedTotalHeight = 0.0

    var renderData = emptyList<T>()
    var renderStates: Array<RowState?> = arrayOfNulls(500)

    var firstAttachedRowIndex = 0
    var attachedRowCount = 0

    // Reference: http://www.html5rocks.com/en/tutorials/speed/animations/
    var lastKnownScrollPosition = 0.0
    var ticking = false

    val columnNumber = 10

    val ROW_INDEX = "zkri"
    val addAboveCount = (window.outerHeight / estimatedRowHeight).toInt()
    val addBelowCount = addAboveCount

    override fun onCreate() {
        estimatedTotalHeight = data.size * estimatedRowHeight

        + styles.outerContainer

        + contentContainer.apply {
            if (trace) println("contentContainer.id = $id  document.getElementById('zk-$id').scrollTop = ")

            + styles.contentContainer

            + table(styles.table) {
                buildPoint.style.cssText = "grid-template-columns: repeat(10, 100px)"
                + thead {
                    + styles.noSelect
                    repeat(columnNumber) { + th { + it.toString() } }
                }
                + tbody {

                }.also {
                    tableBodyElement = it
                }
            }.also {
                tableElement = it
            }

            on("scroll") {
                lastKnownScrollPosition = contentContainer.element.scrollTop.let { if (it < 0) 0.0 else it } // Safari may have negative scrollTop

                if (! ticking) {
                    window.requestAnimationFrame {
                        onScroll(lastKnownScrollPosition)
                        ticking = false
                    }
                    ticking = true
                }
            }
        }

        firstAttachedRowIndex = 0
        attachedRowCount = addAboveCount

        val topPlaceHolderRow = tableBodyElement.appendChild(document.createElement("tr"))
        repeat(columnNumber) {
            topPlaceHolderRow.appendChild(document.createElement("td")).also { cell ->
                cell as HTMLTableCellElement
                cell.style.border = "none"
            }
        }

        adjustTopPlaceHolder()

        val bottomPlaceHolderRow = tableBodyElement.appendChild(document.createElement("tr"))
        repeat(columnNumber) {
            bottomPlaceHolderRow.appendChild(document.createElement("td")).also { cell ->
                cell as HTMLTableCellElement
                cell.style.border = "none"
            }
        }

        adjustBottomPlaceHolder()

        attach(firstAttachedRowIndex, attachedRowCount)
    }

    /**
     * Attaches a row to the table. Attached rows are not necessarily visible for
     * the user (they may be scrolled out) but they are added to the DOM.
     */
    fun attachRow(index: Int) {
        // Get the state of the row or create a new one if it doesn't exist yet
        val rowState = renderStates[index] ?: RowState().also { renderStates[index] = it }

        // Render and attach the row
        rowState.apply {
            render(index).also {
                var row = tableBodyElement.firstElementChild as HTMLTableRowElement?
                var added = false
                while (row != null) {
                    val ridx = row.dataset[ROW_INDEX]?.toInt()
                    if (ridx != null && ridx > index) {
                        tableBodyElement.insertBefore(it.element, row)
                        added = true
                        break
                    }
                    row = row.nextElementSibling as HTMLTableRowElement?
                }

                if (! added) {
                    tableBodyElement.insertBefore(it.element, tableBodyElement.lastElementChild)
                }
            }
            attached = true
        }
    }

    /**
     * Renders the row. Caches the result and returns with it for subsequent calls
     * on the same row.
     *
     * @param index Index of the row in state.rowStates
     */
    fun RowState.render(index: Int): ZkElement {
        element?.let { return it }

        element = ZkElement(document.createElement("tr") as HTMLElement) build {

            repeat(columnNumber) {
                + td {
                    height = (50.0 + (index % 20) * 2).px
                    + "cell ${data[index]}:$it"
                }
            }

            element.dataset[ROW_INDEX] = index.toString()
        }

        return element !!
    }

    fun onScroll(scrollTop: Double) {
        val viewHeight = viewHeight() // height of the area that shows rows
        val attachedHeight = attachedHeight()

        val topRowCount = firstAttachedRowIndex
        val bottomRowCount = renderData.size - firstAttachedRowIndex - attachedRowCount

        val topBoundary = estimatedRowHeight * topRowCount     // offset to the start of the first rendered row
        val bottomBoundary = topBoundary + attachedHeight      // offset to the end of the last rendered row

        //if (trace) println("scrollTop: $scrollTop viewHeight: $viewHeight attachedHeight: $attachedHeight topBoundary: $topBoundary, bottomBoundary: $bottomBoundary")

        when {
            // above attached rows, no attached row on screen
            scrollTop + viewHeight < topBoundary -> fullEmptyAbove()

            // below attached rows, no attached row on screen
            scrollTop > bottomBoundary -> fullEmptyBelow(scrollTop, attachedHeight)

            // above attached rows, EMPTY area between attached rows and scrollTop
            scrollTop < topBoundary -> partialEmptyAbove(scrollTop, attachedHeight)

            // below attached rows, EMPTY area between attached rows and scrollTop
            scrollTop < bottomBoundary && bottomBoundary < scrollTop + viewHeight -> partialEmptyBelow(attachedHeight)

            // there is no empty area
            else -> noEmpty()
        }
    }

    var trace = true

    fun fullEmptyAbove() {
        if (trace) println("fullEmptyAbove")
    }

    fun fullEmptyBelow(scrollTop: Double, originalAttachedHeight: Double) {
        if (trace) println("fullEmptyBelow")

        removeAllAttached()

        val currentTotalHeight = data.size * estimatedRowHeight - (attachedRowCount * estimatedRowHeight) + originalAttachedHeight
        val scrollPercentage = scrollTop / currentTotalHeight
        firstAttachedRowIndex = floor(data.size * scrollPercentage).toInt()
        attachedRowCount = min(data.size - firstAttachedRowIndex, addBelowCount)

        println("currentTotalHeight: $currentTotalHeight  scrollTop: $scrollTop firstAttachedRowIndex: $firstAttachedRowIndex attachedRowCount: $attachedRowCount")

        attach(firstAttachedRowIndex, attachedRowCount)

        adjustTopPlaceHolder()
        adjustBottomPlaceHolder()
        contentContainer.element.scrollTo(0.0, firstAttachedRowIndex * estimatedRowHeight)
    }

    fun attach(start : Int, count : Int) {
        (start until start + count).forEach {
            attachRow(it)
        }
    }

    fun removeAllAttached() {
        val start = firstAttachedRowIndex
        val end = start + attachedRowCount

        (start until end).forEach { index ->
            renderStates[index] !!.let { state ->
                state.element !!.element.remove()
            }
        }
    }

    fun partialEmptyAbove(scrollTop: Double, originalAttachedHeight: Double) {
        if (trace) println("partialEmptyAbove")

        // Calculate the number of rows to attach, update fields, attach the rows

        val originalFirstAttachedRowIndex = firstAttachedRowIndex
        val originalAttachedRowCount = attachedRowCount

        firstAttachedRowIndex = max(originalFirstAttachedRowIndex - addAboveCount, 0)
        attachedRowCount = originalAttachedRowCount + (originalFirstAttachedRowIndex - firstAttachedRowIndex)

        attach(firstAttachedRowIndex, attachedRowCount - originalAttachedRowCount)

        // Calculate new attached height, so we can adjust the scroll top. This adjustment is
        // necessary because the actual height of the attached rows may be different from
        // the estimated row height. This difference would cause the rows already shown to
        // change position on the screen, we don't want that.

        val newAttachedHeight = attachedHeight()

        val estimatedAddition = (originalFirstAttachedRowIndex - firstAttachedRowIndex) * estimatedRowHeight
        val actualAddition = newAttachedHeight - originalAttachedHeight

        contentContainer.element.scrollTop = scrollTop + (actualAddition - estimatedAddition)

        adjustTopPlaceHolder()

        if (trace) println("newAttachedHeight: $newAttachedHeight oldAttachedHeight: $originalAttachedHeight actualAddition : $actualAddition estimatedAddition : $estimatedAddition")

    }

    fun adjustTopPlaceHolder() {
        var placeHolderCell = tableBodyElement.firstElementChild?.firstElementChild as HTMLTableCellElement?
        val placeHolderHeight = (estimatedRowHeight * firstAttachedRowIndex).px
        while (placeHolderCell != null) {
            placeHolderCell.style.minHeight = placeHolderHeight
            placeHolderCell.style.height = placeHolderHeight
            placeHolderCell = placeHolderCell.nextElementSibling as HTMLTableCellElement?
        }
    }

    fun partialEmptyBelow(originalAttachedHeight: Double) {
        if (trace) println("partialEmptyBelow")

        // Calculate the number of rows to attach, update fields, attach the rows

        val originalAttachedRowCount = attachedRowCount

        attachedRowCount = min(attachedRowCount + addBelowCount, data.size - firstAttachedRowIndex) // FIXME check boundaries

        attach(firstAttachedRowIndex + originalAttachedRowCount, attachedRowCount - originalAttachedRowCount)

        adjustBottomPlaceHolder()

        if (trace) println("newAttachedRowCount: $attachedRowCount originalAttachedRowCount: $originalAttachedRowCount")

    }

    fun adjustBottomPlaceHolder() {
        var placeHolderCell = tableBodyElement.lastElementChild?.firstElementChild as HTMLTableCellElement?
        val placeHolderHeight = (estimatedRowHeight * (data.size - (firstAttachedRowIndex + attachedRowCount))).px
        while (placeHolderCell != null) {
            placeHolderCell.style.minHeight = placeHolderHeight
            placeHolderCell.style.height = placeHolderHeight
            placeHolderCell = placeHolderCell.nextElementSibling as HTMLTableCellElement?
        }
    }

    fun noEmpty() {
        //if (trace) println("noEmpty")
    }


    /**
     * Calculates height of the area that shows rows.
     */
    fun viewHeight(): Double {
        val containerHeight = contentContainer.element.getBoundingClientRect().height
        val headerHeight = tableElement.firstElementChild?.firstElementChild?.getBoundingClientRect()?.height ?: styles.rowHeight.toDouble()

        // if (trace) println("contentContainer height: $containerHeight thead height: $headerHeight")

        // TODO subtract footer if needed
        return containerHeight - headerHeight
    }

    fun attachedHeight(): Double {
        val start = firstAttachedRowIndex
        val end = start + attachedRowCount

        return (start until end).sumOf { index ->
            renderStates[index] !!.let { state ->
                state.height ?: state.element !!.element.firstElementChild !!.getBoundingClientRect().height.also { state.height = it }
            }
        }
    }
}

class RowState {
    var attached = false
    var adjusted = false
    var visible = false
    var height: Double? = null
    var element: ZkElement? = null
}

