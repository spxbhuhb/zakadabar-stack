/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.stack.frontend.elements

import kotlinx.browser.document
import kotlinx.dom.clear
import org.w3c.dom.*
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.util.PublicApi
import kotlin.math.max

open class SimpleElement(
    val element: HTMLElement = document.createElement("div") as HTMLElement
) {

    companion object {
        var nextId: Long = 1L
            get() = field ++

        var addKClass = false

        /**
         * Align the width of two elements. The [calc] function is used to calculate the aligned
         * width from the with of the two elements.
         *
         * Sets inline styles minWidth, width, maxWidth of the.
         *
         * @param  first   First element, does nothing when its null.
         * @param  second  Second element, does nothing when its null.
         * @param  calc    Calculation function, defaults to [max].
         */
        @PublicApi
        fun alignWidth(first: SimpleElement?, second: SimpleElement?, calc: (Double, Double) -> Double = ::max) {
            if (first == null || second == null) return

            val firstWidth = first.element.getBoundingClientRect().width
            val secondWidth = second.element.getBoundingClientRect().width

            val alignedWidthPx = "${calc(firstWidth, secondWidth)}px"

            with(first.element.style) {
                minWidth = alignedWidthPx
                width = alignedWidthPx
                maxWidth = alignedWidthPx
            }

            with(second.element.style) {
                minWidth = alignedWidthPx
                width = alignedWidthPx
                maxWidth = alignedWidthPx
            }
        }

    }

    val id = nextId

    init {
        element.id = id.toString() // TODO do we want a prefix here?
        if (addKClass) element.dataset["kclass"] = this::class.simpleName.toString()
    }

    open fun init(): SimpleElement {
        return this
    }

    open fun cleanup(): SimpleElement {
        element.clear()
        return this
    }

    open fun dump(indent: String) = "$indent${this::class.simpleName}(id=${element.id})"

    // ---- DOM Builder

    /**
     * Build a structure of elements inside this element.
     *
     * @param  build      Builder function.
     */
    open infix fun build(build: DOMBuilder.() -> Unit): SimpleElement {
        val builder = DOMBuilder(element)
        builder.build()
        return this
    }


    // ---- Shorthands for DOM data --------

    /**
     * Shorthand for the innerHTML property of the HTML [element].
     */
    var innerHTML: String
        inline get() = element.innerHTML
        inline set(value) {
            element.innerHTML = value
        }

    /**
     * Shorthand for the innerText property of the HTML [element].
     */
    var innerText: String
        inline get() = element.innerText
        inline set(value) {
            element.innerText = value
        }

    /**
     * Shorthand for the className property of the HTML [element].
     */
    var className: String
        inline get() = element.className
        inline set(value) {
            element.className = value
        }

    /**
     * Shorthand for the classList property of the HTML [element].
     */
    val classList: DOMTokenList
        inline get() = element.classList

    // ---- Utilities ----------
    // IMPORTANT If you add one don't forget to add it to ComplexElement also with ComplexElement return type.

    /**
     * Hides the HTML [element] by adding coreClasses.hidden to the class list.
     */
    open fun hide(): SimpleElement {
        classList.add(coreClasses.hidden)
        return this
    }

    /**
     * Shows the HTML [element] by removing coreClasses.hidden from the class list.
     */
    open fun show(): SimpleElement {
        classList.remove(coreClasses.hidden)
        return this
    }

    /**
     * Shorthand for the focus function of the HTML [element].
     */
    open fun focus(): SimpleElement {
        element.focus()
        return this
    }

    open infix fun marginRight(size: Any): SimpleElement {
        element.style.marginRight = if (size is Int) "${size}px" else size.toString()
        return this
    }

    open infix fun marginBottom(size: Any): SimpleElement {
        element.style.marginBottom = if (size is Int) "${size}px" else size.toString()
        return this
    }

    open infix fun width(value: Any): SimpleElement {
        if (value == "100%") {
            classList += coreClasses.w100
        } else {
            element.style.width = value.toString()
        }
        return this
    }

    open infix fun height(value: Any): SimpleElement {
        if (value == "100%") {
            classList += coreClasses.h100
        } else {
            element.style.height = value.toString()
        }
        return this
    }

    open infix fun flex(value: String): SimpleElement {
        if (value == "grow") {
            classList += coreClasses.grow
        } else {
            throw RuntimeException("invalid flex value: $value")
        }
        return this
    }

    /**
     * Main content on the page, adds [coreClasses.mainContent] class
     * to the element.
     */
    @Suppress("KDocUnresolvedReference") // it is there
    open fun mainContent(): SimpleElement {
        classList += coreClasses.mainContent
        return this
    }

    /**
     * Add a CSS class to the class list of this element.
     *
     * @param className Name of the class to add.
     *
     * @return the element itself
     */
    open fun withClass(className: String): SimpleElement {
        element.classList.add(className)
        return this
    }

    open infix fun cssClass(className: String): SimpleElement {
        element.classList.add(className)
        return this
    }

    /**
     * Add CSS classes to the class list of this element.
     *
     * @param classNames Name of the classes to add.
     *
     * @return the element itself
     */
    open fun withClass(vararg classNames: String): SimpleElement {
        element.classList.add(*classNames)
        return this
    }

    /**
     * Add CSS class to the class list of this element if there is no class added yet.
     *
     * @param className Name of the class to add.
     *
     * @return the element itself
     */
    open fun withOptionalClass(className: String): SimpleElement {
        if (element.className.isBlank()) {
            element.className = className
        }
        return this
    }

    fun hasClass() = ! className.isBlank()

    operator fun DOMTokenList.plusAssign(token: String?) {
        if (token != null) this.add(token)
    }

    operator fun DOMTokenList.plusAssign(tokens: Array<out String>) {
        tokens.forEach { this.add(it) }
    }

    operator fun DOMTokenList.minusAssign(token: String?) {
        if (token != null) this.remove(token)
    }
}