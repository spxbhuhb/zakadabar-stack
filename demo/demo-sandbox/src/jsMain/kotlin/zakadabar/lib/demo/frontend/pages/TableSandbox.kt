/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend.pages

import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import org.w3c.dom.set
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.page.ZkPage
import zakadabar.core.browser.util.log
import zakadabar.core.browser.util.w3c.IntersectionObserver
import zakadabar.core.browser.util.w3c.IntersectionObserverEntry
import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.*

var sandboxStyles by cssStyleSheet(SandboxStyles())

open class SandboxStyles : ZkCssStyleSheet() {

    open var defaultRowHeight by cssParameter { 50.px }

    open val page by cssClass {
        backgroundColor = ZkColors.Blue.a200
    }

    open val container by cssClass {
        + OverflowY.scroll
        margin = 20.px
        height = 500.px
    }

    open val row by cssClass {
        minHeight = defaultRowHeight
        paddingLeft = 10.px
        borderBottom = "1px solid black"
        backgroundColor = ZkColors.white
    }

}

class TableSandbox : ZkPage() {

    val data = (1..500).map { it - 1 }

    val container = ZkElement()
    val content = ZkElement()

    val state = TableState()

    var observer = makeObserver()

    override fun onCreate() {
        state.estimatedTotalHeight = data.size * state.estimatedRowHeight
        state.rowStates = arrayOfNulls(data.size)


        + sandboxStyles.page

        + container.apply {

            + sandboxStyles.container

            + content.apply {
                height = state.estimatedTotalHeight.px
                (0..15).forEach {
                    attachRow(it)
                }
            }
        }
    }

    fun ZkElement.attachRow(index: Int) {
        val rowState = state.rowStates[index] ?: RowState().also { state.rowStates[index] = it }

        rowState.apply {
            ensureRender(index)
            attached = true
        }

    }

    fun RowState.ensureRender(index: Int) {
        if (render != null) return

        render = ZkElement() build {
            + row {
                + sandboxStyles.row
                height = (50 + (index % 20) * 2).px

                repeat(10) {
                    + div {
                        + "row ${data[index]}"
                    }
                }
                element.dataset["zkri"] = index.toString()
            }
        }

        this@TableSandbox.content += render
        observer.observe(render !!.element)
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
            }
            window.requestAnimationFrame {
                state.rowStates.forEach { rs ->
                    rs?.render?.let {
                        rs.height = it.element.getBoundingClientRect().height
                        if (!rs.adjusted && rs.height != state.estimatedRowHeight) {
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

    fun HTMLElement.isLast() = (nextElementSibling == null)

    fun makeObserver(): IntersectionObserver {
        val options: dynamic = object {}
//        options["root"] = element.parentElement
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
