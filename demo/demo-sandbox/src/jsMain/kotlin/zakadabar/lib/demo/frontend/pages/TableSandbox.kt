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
import zakadabar.core.browser.util.log
import zakadabar.core.browser.util.w3c.IntersectionObserver
import zakadabar.core.browser.util.w3c.IntersectionObserverEntry
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.px

var styles by cssStyleSheet(SandboxStyles())

open class SandboxStyles : ZkTableStyles() {

}

class TableSandbox : ZkPage() {

    val data = (1..500).map { it - 1 }

    val contentContainer = ZkElement()
    lateinit var tableElement: HTMLTableElement
    lateinit var tableBodyElement : HTMLTableSectionElement

    val content = ZkElement()

    val state = TableState()

    var observer = makeObserver()

    override fun onCreate() {
        state.estimatedTotalHeight = data.size * state.estimatedRowHeight
        state.rowStates = arrayOfNulls(data.size)

        + styles.outerContainer

        + contentContainer.apply {
            + styles.contentContainer

            + table(styles.table) {
                buildPoint.style.cssText = "grid-template-columns: repeat(10, 100px)"
                + thead {
                    + styles.noSelect
                    repeat(10) { + th { + it.toString() } }
                }
                + tbody {

                }.also {
                    tableBodyElement = it
                }
            }.also {
                tableElement = it
            }
        }

        (0..15).forEach {
            attachRow(it)
        }

        val bottomPlaceHolder = ZkElement(document.createElement("tr") as HTMLElement) build {
            repeat(10) { + td { height = state.estimatedTotalHeight.px } }
        }

        tableBodyElement.appendChild(bottomPlaceHolder.element)
    }

    /**
     * Attaches a row to the table. Attached rows are not necessarily visible for
     * the user (they may be scrolled out) but they are added to the DOM.
     */
    fun attachRow(index: Int) {
        // Get the state of the row or create a new one if it doesn't exist yet
        val rowState = state.rowStates[index] ?: RowState().also { state.rowStates[index] = it }

        // Render and attach the row
        rowState.apply {
            ensureRender(index).also {
                tableBodyElement.appendChild(it.element)
                observer.observe(it.element)
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
    fun RowState.ensureRender(index: Int): ZkElement {
        render?.let { return it }

        render = ZkElement(document.createElement("tr") as HTMLElement) build {

            repeat(10) {
                + td {
                    height = (50 + (index % 20) * 2).px
                    + "cell ${data[index]}:$it"
                }
            }

            element.dataset["zkri"] = index.toString()
        }

        return render !!
    }

    fun observerCallback(entries: Array<IntersectionObserverEntry>, o: IntersectionObserver) {
        try {
            entries.forEach {
                val target = it.target as HTMLElement
                val rowIndex = target.dataset["zkri"]?.toInt() ?: return@forEach

                state.rowStates[rowIndex]?.visible = it.isIntersecting

                if (it.isIntersecting && target.isLast() && rowIndex + 1 < data.size) {
                    attachRow(rowIndex + 1)
                }
                println(it.target.id + " " + it.isIntersecting)
            }
            window.requestAnimationFrame {
                state.rowStates.forEach { rs ->
                    rs?.render?.let {
                        rs.height = it.element.getBoundingClientRect().height
                        if (! rs.adjusted && rs.height != state.estimatedRowHeight) {
                            state.estimatedTotalHeight += (rs.height - state.estimatedRowHeight)
                            rs.adjusted = true
                            println("height: ${state.estimatedTotalHeight}")
                            content.height = state.estimatedTotalHeight.px
                        }
                    }
                }
            }
        } catch (ex: Throwable) {
            log(ex)
        }
    }

    fun HTMLElement.isLast() = (nextElementSibling?.nextElementSibling == null)

    fun makeObserver(): IntersectionObserver {
        val options: dynamic = object {}
        options["root"] = element
//        options["rootMargin"] = "200px" // pre-load when the area is as close as 200px to the screen
//        options["threshold"] = 0 // pre-load when even a pixel of the area is about to be shown

        return IntersectionObserver(::observerCallback, options)
    }


}

class RowState {
    var attached = false
    var adjusted = false
    var visible = false
    var height = 0.0
    var render: ZkElement? = null
}

class TableState {
    var estimatedRowHeight = 50.0
    var estimatedTotalHeight = 0.0
    var rowStates: Array<RowState?> = arrayOfNulls(0)
}
