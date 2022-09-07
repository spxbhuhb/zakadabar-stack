/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.layout

import kotlinx.browser.document
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
import kotlin.math.max
import kotlin.math.min

open class SuiLayoutSlider(
    val layout: SuiDefaultLayout,
    var minSize: Double = Double.NaN,
    val maxSize: Double = Double.POSITIVE_INFINITY,
) : ZkElement() {

    protected var active = false
    var position = 0.0

    override fun onCreate() {
        + layout.styles.sliderContainer
        on("mousedown", ::onMouseDown)
        layout.on("mouseup", ::onMouseUp)
        layout.on("mousemove", ::onMouseMove)
        layout.on("mouseleave", ::onMouseLeave)
    }

    protected fun onMouseDown(event: Event) {
        active = true
        document.body!!.classList += layout.styles.noSelect
        position = getPosition(event)
    }

    protected fun onMouseUp(@Suppress("UNUSED_PARAMETER") event: Event) {
        active = false
        document.body!!.classList -= layout.styles.noSelect
    }

    protected fun onMouseLeave(@Suppress("UNUSED_PARAMETER") event: Event) {
        active = false
        document.body!!.classList -= layout.styles.noSelect
    }

    protected fun onMouseMove(event: Event) {
        if (! active) return

        val newPosition = getPosition(event)

        val delta = newPosition - position

        position = newPosition

        onSliderMove(delta)
    }

    protected fun onSliderMove(delta: Double) {
        if (minSize.isNaN()) {
            minSize = layout.sideBar.element.getBoundingClientRect().width
        }

        val current = if (layout.sidebarWidth.isNaN()) {
            layout.sideBar.element.getBoundingClientRect().width
        } else {
            layout.sidebarWidth
        }

        layout.sidebarWidth = newSize(current, delta)
    }

    protected fun getPosition(event: Event): Double {
        event as MouseEvent
        return event.x
    }

    protected fun newSize(size: Double, delta: Double): Double {

        var newSize = size + delta

        newSize = max(minSize, newSize)
        newSize = min(maxSize, newSize)

        return newSize
    }

}
