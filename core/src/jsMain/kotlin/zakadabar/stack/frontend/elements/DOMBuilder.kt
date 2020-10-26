/*
 * Copyright © 2020, Simplexion, Hungary and contributors
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
import kotlinx.dom.appendText
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.Event
import zakadabar.stack.Stack
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.messaging.Message
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass

/**
 * A class to build DOM structures easily.
 *
 * Check [current] to see how to add HTML tags from the builder functions.
 */
@Suppress("KDocUnresolvedReference")
class DOMBuilder(
    /**
     * The [HTMLElement] the current build function builds. May be used to
     * set additional tags. For example:
     *
     * ```kotlin
     *
     * + image("/my-picture.png") {
     *     current.style.width = "10"
     * }
     * ```
     */
    var current: HTMLElement,
    private val parentComplex: ComplexElement? = null,
) {

    /**
     * Creates SimpleElement and executes the builder function on it.
     *
     * @param  className  CSS class to add. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun div(className: String? = null, build: DOMBuilder.() -> Unit = { }): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += className
        buildElement(e, className, build)
        return e
    }

    /**
     * Creates a "div" [HTMLElement] with [coreClasses.row] added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun row(className: String? = null, build: DOMBuilder.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += coreClasses.row
        buildElement(e, className, build)
        return e
    }

    /**
     * Creates [HTMLElement] with [coreClasses.row].
     */
    fun row(): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += coreClasses.row
        current.appendChild(e)
        return e
    }

    /**
     * Creates "div" [HTMLElement] with [coreClasses.col] added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun col(className: String? = null, build: DOMBuilder.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += coreClasses.col
        buildElement(e, className, build)
        return e
    }

    /**
     * Creates [HTMLElement] with [coreClasses.col].
     */
    fun column(): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += coreClasses.col
        current.appendChild(e)
        return e
    }

    /**
     * Creates "div" [HTMLElement] with height and width passed as parameters.
     *
     * Be careful as Safari (and perhaps Chrome) collapses the gap when you
     * use percent and the parent container is not sized properly.
     *
     * @param  width   Width of the gap. String or Int. Defaults is "100%".
     * @param  height  Height of the gap. String or Int. Defaults is "100%".
     */
    fun gap(width: Any = "100%", height: Any = "100%"): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.style.height = if (height is Int) "${height}px" else height.toString()
        e.style.width = if (width is Int) "${width}px" else width.toString()
        return e
    }

    /**
     * Creates an IMG with a source url. Use [DOMBuilder.current] to add attributes other than the source.
     *
     * ```
     *
     *     with(current as HTMLImageElement) {
     *         width = 100
     *         height = 100
     *     }
     *
     * ```
     *
     * @param  src        Source URL.
     * @param  className  Additional CSS class. Optional.
     * @param  build      Builder function to manage the IMG tag added. Optional.
     */
    @PublicApi
    fun image(src: String, className: String? = null, build: DOMBuilder.() -> Unit = { }): HTMLImageElement {
        val img = document.createElement("img") as HTMLImageElement
        img.src = src
        buildElement(img, className, build)
        return img
    }

    /**
     * Adds an IMG to the current element with an entity id. Converts the entity id
     * into a Stack entity URL to use as source. Use [DOMBuilder.current] to add
     * attributes other than the source.
     *
     * ```
     *
     *     with(current as HTMLImageElement) {
     *         width = 100
     *         height = 100
     *     }
     *
     * ```
     *
     * @param  entityId   Id of the image entity. Nothing will be added when null.
     * @param  className  Additional CSS class. Optional.
     * @param  build      Builder function to manage the IMG tag added. Optional.
     */
    fun image(entityId: Long, className: String? = null, build: DOMBuilder.() -> Unit = { }): HTMLImageElement {
        val img = document.createElement("img") as HTMLImageElement
        img.src = "/api/${Stack.shid}/entities/$entityId/revisions/last"
        buildElement(img, className, build)
        return img
    }

    /**
     * Creates an unnamed [ComplexElement].
     */
    fun complex(): ComplexElement {
        val ce = ComplexElement()
        current.appendChild(ce.element)
        parentComplex?.childElements?.plusAssign(ce)
        // no need for init as it is empty
        return ce
    }

    /**
     * Adds a browser event listener (with the event parameter) to the current element.
     *
     * @param  type      Name of the browser event like "click".
     * @param  listener  Event listener function.
     */
    fun on(type: String, listener: ((Event) -> Unit)?) {
        parentComplex !!.on(type, listener)
    }

    /**
     * Adds a browser event listener (without the event parameter) to the current element.
     *
     * @param  type      Name of the browser event like "click".
     * @param  listener  Event listener function.
     */
    fun on(type: String, listener: (() -> Unit)?) {
        parentComplex !!.on(type, listener)
    }

    /**
     * Adds a message handler to the current element.
     *
     * @param  type      Class of the message to handle.
     * @param  handler   The message handler function.
     */
    fun <T : Message> on(type: KClass<T>, handler: (T) -> Unit) {
        parentComplex !!.on(type, handler)
    }

    /**
     * Creates a SPAN with [SimpleElement.innerHTML] set to the string passed.
     * The string is **not escaped**. You have to escape it yourself.
     */
    operator fun String.not() : HTMLElement {
        val e = document.createElement("span")
        e.innerHTML = this
        current.append(e)
        return e as HTMLElement
    }

    /**
     * Creates a text node using [HTMLElement.appendText] with the string passed as content.
     * No need for escape.
     */
    operator fun String.unaryPlus() : Element {
        return current.appendText(this)
    }

    /**
     * Adds the HTML element.
     */
    operator fun HTMLElement.unaryPlus(): HTMLElement {
        current.appendChild(this)
        return this
    }

    /**
     * Build a structure of elements inside this element.
     *
     * @param  build      Builder function.
     */
    infix fun HTMLElement.build(build: DOMBuilder.() -> Unit): HTMLElement {
        val builder = DOMBuilder(this, parentComplex)
        builder.build()
        return this
    }

    /**
     * Build a structure of elements inside this element.
     *
     * @param  build      Builder function.
     */
    infix fun Element.cssClass(className: String): Element {
        this.classList += className
        return this
    }

    /**
     * Build a structure of elements inside this element.
     *
     * @param  build      Builder function.
     */
    infix fun HTMLElement.cssClass(className: String): HTMLElement {
        this.classList += className
        return this
    }

    /**
     * Adds a [SimpleElement].
     */
    operator fun SimpleElement.unaryPlus(): SimpleElement {
        current.appendChild(this.element)
        this.init()
        return this
    }

    /**
     * Adds a [ComplexElement].
     */
    operator fun ComplexElement.unaryPlus(): ComplexElement {
        require(parentComplex != null) { "cannot add complex element to simple element" }
        current.appendChild(this.element)
        parentComplex.childElements += this.init()
        return this
    }

    private fun buildElement(e: HTMLElement, className: String?, build: DOMBuilder.() -> Unit) {
        if (className != null) e.classList.add(className)
        val original = current
        current = e
        build()
        current = original
    }

}