/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.popup

import kotlinx.browser.window
import org.w3c.dom.HTMLElement

/**
 * Positions [popupElement] relative to [anchorElement] according to the space
 * available and the desired height of the [popupElement].
 *
 * See README.md for more information.
 */
fun alignPopup(popupElement: HTMLElement, anchorElement: HTMLElement, minHeight: Int) {
    val anchorRect = anchorElement.getBoundingClientRect()
    val popupRect = popupElement.getBoundingClientRect()

    val spaceBelow = window.innerHeight - (anchorRect.top + anchorRect.height)
    val spaceAbove = anchorRect.top
    val maxHeight = popupRect.height

    // console.log(minHeight, anchorRect, popupRect, spaceBelow, spaceAbove, maxHeight)

    when {
        spaceBelow > minHeight -> {
            if (spaceBelow > maxHeight) {
                popupElement.style.height = "${maxHeight}px"
                popupElement.style.top = "${anchorRect.top}px"
            } else {
                popupElement.style.maxHeight = "${spaceBelow}px"
                popupElement.style.top = "${anchorRect.top}px"
            }
        }

        spaceAbove > minHeight -> {
            if (spaceAbove > maxHeight) {
                popupElement.style.height = "${maxHeight}px"
                popupElement.style.top = "${anchorRect.top - maxHeight}px"
            } else {
                popupElement.style.height = "${spaceAbove}px"
                popupElement.style.top = "0px"
            }
        }
        // TODO try to position the element in the middle.

        // fallback, there is not enough space, just put it there and shrink it
        else -> {
            popupElement.style.height = "${spaceBelow}px"
            popupElement.style.top = "-${spaceAbove}px"
        }
    }


}