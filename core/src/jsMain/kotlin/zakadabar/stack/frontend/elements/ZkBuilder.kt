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
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.util.PublicApi

/**
 * Creates an anonymous [ZkElement] and calls [ZkElement.launchBuild] on it with
 * the [builder] function.
 *
 * Use this function when you need to fetch data asynchronously during building
 * the element.
 */
fun launchBuildNew(builder: suspend ZkBuilder.() -> Unit) = ZkElement().launchBuild(builder)

/**
 * Creates an anonymous [ZkElement] and calls [ZkElement.build] on it with
 * the [builder] function.
 *
 * Use this function when there is no need to asynchronous data fetch.
 */
fun buildNew(builder: ZkBuilder.() -> Unit) = ZkElement().build(builder)

/**
 * Provides programmatic builder functionality to create the DOM and link
 * it to [ZkElement] implementations.
 *
 * @property  zkElement    The [ZkElement] being built. Does not change in the scope
 *                         of one builder.
 *
 * @property  htmlElement  The [HTMLElement] being built. Changes frequently as
 *                         the builder goes on.
 */
class ZkBuilder(
    val zkElement: ZkElement,
    var htmlElement: HTMLElement
) {

    private fun runBuild(e: HTMLElement, className: String?, build: ZkBuilder.() -> Unit) {
        if (className != null) e.classList.add(className)
        val original = htmlElement
        htmlElement = e
        build()
        htmlElement = original
    }

    /**
     * Creates "div" [HTMLElement] and executes the builder function on it.
     *
     * @param  className  CSS class to add. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun div(className: String? = null, build: ZkBuilder.() -> Unit = { }): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates a "div" [HTMLElement] with coreClasses.row added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun row(className: String? = null, build: ZkBuilder.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += coreClasses.row
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates "div" [HTMLElement] with coreClasses.column added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun column(className: String? = null, build: ZkBuilder.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += coreClasses.column
        runBuild(e, className, build)
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
     * Creates an IMG with a source url. Use [ZkBuilder.htmlElement] to add attributes other than the source.
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
    fun image(src: String, className: String? = null, build: ZkBuilder.() -> Unit = { }): HTMLImageElement {
        val img = document.createElement("img") as HTMLImageElement
        img.src = src
        runBuild(img, className, build)
        return img
    }

    /**
     * Adds an IMG to the current element with an entity id. Converts the entity id
     * into a Stack entity URL to use as source. Use [ZkBuilder.htmlElement] to add
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
    fun image(entityId: Long, className: String? = null, build: ZkBuilder.() -> Unit = { }): HTMLImageElement {
        val img = document.createElement("img") as HTMLImageElement
        img.src = EntityRecordDto.revisionUrl(entityId)
        runBuild(img, className, build)
        return img
    }

    /**
     * Creates an unnamed [ZkElement].
     */
    fun element(): ZkElement {
        val ce = ZkElement()
        htmlElement.appendChild(ce.element)
        zkElement.childElements.plusAssign(ce)
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
        zkElement.on(type, listener)
    }

    /**
     * Creates a SPAN with [HTMLElement.innerHTML] set to the string passed.
     * The string is **not escaped**. You have to escape it yourself.
     */
    operator fun String.not(): HTMLElement {
        val e = document.createElement("span")
        e.innerHTML = this
        htmlElement.append(e)
        return e as HTMLElement
    }

    /**
     * Creates a text node using [HTMLElement.appendText] with the string passed as content.
     * No need for escape.
     */
    operator fun String.unaryPlus(): Element {
        return htmlElement.appendText(this)
    }

    /**
     * Adds an HTML element.
     */
    operator fun HTMLElement.unaryPlus(): HTMLElement {
        htmlElement.appendChild(this)
        return this
    }

    /**
     * Adds a [ZkElement].
     */
    operator fun ZkElement.unaryPlus(): ZkElement {
        htmlElement.appendChild(this.element)
        zkElement.childElements += this.init()
        return this
    }

    /**
     * Append the given class to the class list of the element.
     *
     * @param  className  Name of the class to append.
     */
    infix fun Element.cssClass(className: String): Element {
        this.classList += className
        return this
    }

    /**
     * Append the given class to the class list of the element.
     *
     * @param  className  Name of the class to append.
     */
    infix fun HTMLElement.cssClass(className: String): HTMLElement {
        this.classList += className
        return this
    }

    /**
     * Creates "div" [HTMLElement] and executes the builder function on it.
     *
     * @param  className  CSS class to add. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    infix fun HTMLElement.build(build: ZkBuilder.() -> Unit): HTMLElement {
        runBuild(this, className, build)
        return this
    }

}