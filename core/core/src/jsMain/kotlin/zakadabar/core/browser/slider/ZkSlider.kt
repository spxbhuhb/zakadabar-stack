/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.slider

import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import zakadabar.core.browser.ZkElement
import zakadabar.core.util.PublicApi
import kotlin.math.max
import kotlin.math.min

/**
 * A draggable slider to resize the subject element when the
 * user clicks on the slider and moves the mouse while keeping
 * the button pressed.
 *
 * Threshold methods are called when the threshold is crossed.
 *
 * @property  container     The container that contains the subject and the slider.
 * @property  subject       The element to resize.
 * @property  attached      Elements to resize together with the subject.
 * @property  orientation   Orientation of the slider. When horizontal the slider
 *                          changes the width property of the subject. When vertical
 *                          the slider changes the height.
 * @property  reverse       When true the subject will shrink instead of grow and vice versa.
 * @property  minSize       Minimum size the subject may have (dimension depends on orientation).
 * @property  maxSize       Maximum size the subject may have (dimension depends on orientation).
 * @property  minRemaining  Minimum remaining size between the container size and subject size
 *                          (dimension depends on orientation)
 * @property  threshold     The threshold for [onBelow] and [onAbove] methods.
 * @property  onBelow       Called when the subject size goes below the threshold.
 * @property  onAbove       Called when the subject size goes above the threshold.
 */
@PublicApi
open class ZkSlider(
    val container: ZkElement,
    val subject: ZkElement,
    val attached: List<ZkElement> = emptyList(),
    val orientation: ZkOrientation = ZkOrientation.Vertical,
    val reverse: Boolean = false,
    val minSize: Double = 26.0, // TODO this comes from the style/theme: header icon dimensions
    val maxSize: Double = Double.POSITIVE_INFINITY,
    val minRemaining: Double = 26.0, // TODO this comes from the style/theme header icon dimensions
    val threshold: Double = 1.0,
    val onBelow: () -> Unit = { },
    val onAbove: () -> Unit = { }
) : ZkElement() {

    protected var active = false
    var position = 0.0

    override fun onCreate() {
        + if (orientation == ZkOrientation.Vertical) zkSliderStyles.verticalSlider else zkSliderStyles.horizontalSlider

        on("mousedown", ::onMouseDown)
        container.on("mouseup", ::onMouseUp)
        container.on("mousemove", ::onMouseMove)
        container.on("mouseleave", ::onMouseLeave)
    }

    protected fun onMouseDown(event: Event) {
        active = true
        position = getPosition(event)
    }

    protected fun onMouseUp(@Suppress("UNUSED_PARAMETER") event: Event) {
        active = false
    }

    protected fun onMouseLeave(@Suppress("UNUSED_PARAMETER") event: Event) {
        active = false
    }

    protected fun onMouseMove(event: Event) {
        if (! active) return

        val newPosition = getPosition(event)

        val delta = if (reverse) position - newPosition else newPosition - position

        position = newPosition

        onSliderMove(delta)
    }

    @Suppress("DuplicatedCode") // silly, with and height are not interchangeable
    protected fun onSliderMove(delta: Double) {
        val boundingRect = subject.element.getBoundingClientRect()
        val containerBoundingRect = container.element.getBoundingClientRect()

        if (orientation == ZkOrientation.Horizontal) {

            val newSizePx = newSize(boundingRect.height, containerBoundingRect.height, delta)

            with(subject.element.style) {
                minHeight = newSizePx
                height = newSizePx
                maxHeight = newSizePx
            }

            attached.forEach {
                with(it.element.style) {
                    minHeight = newSizePx
                    height = newSizePx
                    maxHeight = newSizePx
                }
            }

        } else {

            val newSizePx = newSize(boundingRect.width, containerBoundingRect.width, delta)

            with(subject.element.style) {
                minWidth = newSizePx
                width = newSizePx
                maxWidth = newSizePx
            }

            attached.forEach {
                with(it.element.style) {
                    minWidth = newSizePx
                    width = newSizePx
                    maxWidth = newSizePx
                }
            }
        }

    }

    protected fun getPosition(event: Event): Double {
        event as MouseEvent
        return if (orientation == ZkOrientation.Horizontal) event.y else event.x
    }

    protected fun newSize(size: Double, containerSize: Double, delta: Double): String {

        var newSize = size + delta

        newSize = max(minSize, newSize)
        newSize = min(maxSize, newSize)
        newSize = min(containerSize - minRemaining, newSize)

        if (size > threshold && newSize < threshold) {
            onBelow()
        }

        if (size < threshold && newSize > threshold) {
            onAbove()
        }

        return "${newSize}px"
    }

}
