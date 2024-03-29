/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.misc.dropdown

import org.w3c.dom.events.Event
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.util.PublicApi

/**
 * A wrapper around a simple element which is the [controller] that shows
 * the content when the user clicks on the it.
 *
 * @property   content           The content of the dropdown.
 * @property   controller        The element that controls show / hide of the dropdown.
 * @property   initialPosition   The position where the dropdown shows, may contain "top", "right", "bottom" and/or "left".
 */
@PublicApi
class ZkDropdown(
    val content: ZkElement,
    val controller: ZkElement,
    val initialPosition: String
) : ZkElement() {

    protected var temporaryPosition: String = ""

    val dropdownContent: ZkElement = ZkDropdownContent(content)

    override fun onCreate() {
        this css zkDropdownStyles.dropdown build {
            + controller
            + dropdownContent
        }

        setPositions(initialPosition)

        controller.on("click") { onControllerClick() }

        on("onMouseDown", ::onMouseDown)

        dropdownContent.element.tabIndex = 0
        dropdownContent.on("blur") { close() }
    }

    protected fun setPositions(position: String) {

        val offsetHeight = controller.element.offsetHeight.toString() + "px"
        val offsetWidth = controller.element.offsetWidth.toString() + "px"

        val onTop = "top" in position
        val onRight = "right" in position
        val onBottom = "bottom" in position
        val onLeft = "left" in position

        val vertical = onTop || onBottom
        val horizontal = onLeft || onRight

        when {
            onTop -> {
                setPosition("bottom", offsetHeight)
                setPosition("top", "unset")
            }
            onRight -> {
                setPosition("left", offsetWidth)
                setPosition("right", "unset")
            }
            onBottom -> {
                setPosition("top", offsetHeight)
                setPosition("bottom", "unset")
            }
            onLeft -> {
                setPosition("right", offsetWidth)
                setPosition("left", "unset")
            }
        }

        when {
            "start" in position -> {
                when {
                    vertical -> setPosition("left", "0px")
                    horizontal -> setPosition("top", "0px")
                }
            }
            "end" in position -> {
                when {
                    vertical -> setPosition("right", "0px")
                    horizontal -> setPosition("bottom", "0px")
                }
            }
            "center" in position -> {
                when {
                    vertical -> {
                        setPosition("left", "50%")
                        setPosition("transform", "translateX(-50%)")
                    }
                    horizontal -> {
                        setPosition("top", "50%")
                        setPosition("transform", "translateY(-50%)")
                    }
                }
            }
            else -> {
                if (horizontal) setPosition("top", "0px")
            }
        }

    }

    protected fun setPosition(propName: String, value: String) {
        dropdownContent.element.style.setProperty(propName, value)
    }

    protected fun hasDistance(): Boolean {
        val contentEl = dropdownContent.element
        val controllerEl = controller.element

        val controllerBCR = controllerEl.getBoundingClientRect()

        val contentHeight = contentEl.offsetHeight
        val contentWidth = contentEl.offsetWidth

        val onTop = "top" in initialPosition
        val onRight = "right" in initialPosition
        val onBottom = "bottom" in initialPosition
        val onLeft = "left" in initialPosition

        val remainingDistance = when {
            onTop -> controllerBCR.top - contentHeight
            onRight -> controllerBCR.right - contentWidth
            onBottom -> controllerBCR.bottom - contentHeight
            onLeft -> controllerBCR.left - contentWidth
            else -> - 1
        }

        return remainingDistance.toDouble() >= 0.0

    }

    protected fun onControllerClick() {
        dropdownContent.classList.toggle(zkDropdownStyles.dropdownActive.cssClassname)

        val hasPlace = hasDistance()

        val onTop = "top" in initialPosition
        val onRight = "right" in initialPosition
        val onBottom = "bottom" in initialPosition
        val onLeft = "left" in initialPosition

        temporaryPosition = when {
            ! hasPlace && onTop -> initialPosition.replace("top", "bottom")
            ! hasPlace && onRight -> initialPosition.replace("right", "left")
            ! hasPlace && onBottom -> initialPosition.replace("bottom", "top")
            ! hasPlace && onLeft -> initialPosition.replace("left", "right")
            else -> ""
        }

        setPositions(temporaryPosition)

        if (dropdownContent.classList.contains(zkDropdownStyles.dropdownActive.cssClassname)) {
            dropdownContent.element.focus()
        }

    }

    fun close() {
        dropdownContent.classList -= zkDropdownStyles.dropdownActive
    }

    protected fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}