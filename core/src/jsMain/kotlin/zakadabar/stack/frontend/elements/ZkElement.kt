/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.appendText
import kotlinx.dom.clear
import org.w3c.dom.*
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.elements.ZkClasses.Companion.zkClasses
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.frontend.util.log
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass

/**
 * @property  currentElement  The HTML DOM element that is currently built. This may be
 *                            different than the ZkElement build as DIVs are usually added
 *                            to build the layout of the page.
 */
open class ZkElement(
    val element: HTMLElement = document.createElement("div") as HTMLElement
) {

    companion object {

        var nextId: Long = 1L
            get() = field ++

        var addKClass = false

        /**
         * Creates an anonymous [ZkElement] and calls [ZkElement.launchBuild] on it with
         * the [builder] function.
         *
         * Use this function when you need to fetch data asynchronously during building
         * the element.
         */
        fun launchBuildNew(builder: suspend ZkElement.() -> Unit) = ZkElement().launchBuild(builder)

        /**
         * Creates an anonymous [ZkElement] and calls [ZkElement.build] on it with
         * the [builder] function.
         *
         * Use this function when there is no need to asynchronous data fetch.
         */
        fun buildNew(builder: ZkElement.() -> Unit) = ZkElement().build(builder)

    }

    val id = nextId

    var currentElement = element

    init {
        element.id = "zk-$id"
        if (addKClass) element.dataset["kclass"] = this::class.simpleName.toString()
    }

    /**
     * The event that is currently processed by this element. This variable is here
     * so we don't have to pass event objects all around. The event wrapper function
     * sets/clears this method. When it is null there is no event handling in process.
     */
    var event: Event? = null

    val childElements by lazy { mutableListOf<ZkElement>() }

    private val eventListeners by lazy { mutableListOf<Pair<String, Pair<EventTarget, (Event) -> Unit>>>() }

    /**
     * Display name of the current user.
     */
    val displayName
        get() = Application.executor.account.displayName

    open fun init(): ZkElement {
        return this
    }

    open fun cleanup(): ZkElement {
        clearChildren()
        cleanupEventListeners()
        element.clear()
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
     * Shorthand for the setting the style string of the HTML [element].
     */
    var style: String
        inline get() = element.style.cssText
        inline set(value) {
            element.style.cssText = value
        }

    /**
     * Shorthand for the classList property of the HTML [element].
     */
    val classList: DOMTokenList
        inline get() = element.classList

    // ---- DOM Builder

    open infix fun build(builder: ZkElement.() -> Unit): ZkElement {
        this.builder()
        return this
    }

    open infix fun launchBuild(builder: suspend ZkElement.() -> Unit): ZkElement {
        launch {
            this.builder()
        }
        return this
    }

    fun isShown() = ! element.classList.contains(zkClasses.hidden)

    fun isHidden() = element.classList.contains(zkClasses.hidden)

    fun toggle() = element.classList.toggle(zkClasses.hidden)

    open fun hide(): ZkElement {
        classList.add(zkClasses.hidden)
        return this
    }

    open fun show(): ZkElement {
        classList.remove(zkClasses.hidden)
        return this
    }

    open fun focus(): ZkElement {
        element.focus()
        return this
    }

    infix fun marginRight(size: Any): ZkElement {
        element.style.marginRight = if (size is Int) "${size}px" else size.toString()
        return this
    }

    infix fun marginBottom(size: Any): ZkElement {
        element.style.marginBottom = if (size is Int) "${size}px" else size.toString()
        return this
    }

    infix fun width(value: Any): ZkElement {
        if (value == "100%") {
            classList += zkClasses.w100
        } else {
            element.style.width = value.toString()
        }
        return this
    }

    infix fun height(value: Any): ZkElement {
        if (value == "100%") {
            classList += zkClasses.h100
        } else {
            element.style.height = value.toString()
        }
        return this
    }

    open infix fun flex(value: String): ZkElement {
        if (value == "grow") {
            classList += zkClasses.grow
        } else {
            throw RuntimeException("invalid flex value: $value")
        }
        return this
    }

    fun hasClass() = className.isNotBlank()

    fun withClass(className: String): ZkElement {
        element.classList.add(className)
        return this
    }

    infix fun cssClass(className: String): ZkElement {
        element.classList.add(className)
        return this
    }

    fun withClass(vararg classNames: String): ZkElement {
        element.classList.add(*classNames)
        return this
    }

    fun withOptionalClass(className: String): ZkElement {
        if (element.className.isBlank()) {
            element.className = className
        }
        return this
    }

    // ----  Child elements  --------

    @PublicApi
    fun clearChildren(): ZkElement {
        if (::childElements.isInitialized) {
            childElements.forEach { child -> child.cleanup() }
        }
        childElements.clear()
        return this
    }

    operator fun plusAssign(children: List<ZkElement>) {
        children.forEach { child ->
            this.element.appendChild(child.element)
            childElements += child.init()
        }
    }

    operator fun plusAssign(child: ZkElement?) {
        if (child == null) return
        this.element.appendChild(child.element)
        childElements += child.init()
    }

    @PublicApi
    infix fun insertFirst(child: ZkElement?) {
        if (child == null) return
        this.element.insertBefore(child.element, this.element.firstChild)
        childElements.add(0, child.init())
    }

    fun insertAfter(child: ZkElement?, after: ZkElement?) {
        if (child == null) return
        if (after == null) {
            insertFirst(child)
        } else {
            val index = childElements.indexOf(after)
            when (index) {
                - 1 -> plusAssign(child)
                childElements.lastIndex -> plusAssign(child)
                else -> {
                    this.element.insertBefore(child.element, childElements[index].element.nextElementSibling)
                    childElements.add(index + 1, child.init())
                }
            }
        }
    }

    fun insertBefore(child: ZkElement?, before: ZkElement?) {
        if (child == null) return

        if (before == null) {
            plusAssign(child)
            return
        }

        val index = childElements.indexOf(before)

        if (index == - 1) {
            plusAssign(child)
        } else {
            this.element.insertBefore(child.element, childElements[index].element)
            childElements.add(index, child.init())
        }

    }

    /**
     * Remove a child.
     */
    open operator fun minusAssign(child: ZkElement?) {
        if (child == null) return

        childElements -= child

        child.cleanup()
        child.element.remove()
    }

    /**
     * Remove all children that are of the given class.
     */
    open operator fun minusAssign(clazz: KClass<*>) {
        childElements.filter { clazz.isInstance(it) }.forEach {
            this -= it
        }
    }

    fun hasChildOf(clazz: KClass<*>): Boolean {
        for (child in childElements) {
            if (clazz.isInstance(child)) return true
        }
        return false
    }

    /**
     * Get a child [ZkElement] of the given class. Useful in event handlers
     * to get child element without storing it in a variable.
     *
     * Note that in this example "this" is the ZkBuilder instance.
     * ```
     *
     *    fun updateShip(state: NavState) = launchBuildNew {
     *
     *        + ShipForm(ShipDto.read(state.recordId))
     *
     *        + SimpleButton("back") { Navigation.back() }
     *        + SimpleButton("submit") { zkElement[ShipForm::class].submit() }
     *
     *    }
     *
     * ```
     */
    inline operator fun <reified T : ZkElement> get(kClass: KClass<T>): T {
        return childElements.first { kClass.isInstance(it) } as T
    }

    /**
     * Get the first [ZkElement] by the given CSS class.
     * ```
     */
    inline operator fun <reified T : ZkElement> get(cssClassName: String): T {
        return childElements.first { it.classList.contains(cssClassName) } as T
    }

    // ---- Event listeners ----

    fun on(type: String, listener: (() -> Unit)?) = on(element, type, listener)

    fun on(type: String, listener: ((Event) -> Unit)?) = on(element, type, listener)

    fun on(target: EventTarget, type: String, listener: (() -> Unit)?) {
        if (listener == null) return

        // wrap the listener so we won't have crazy exception strings all around
        // this also helps with removal as the object will be the same as we added

        addWrapped(target, type) { event ->
            try {
                this.event = event
                listener()
            } catch (ex: Throwable) {
                onException(ex)
            } finally {
                this.event = null
            }
        }
    }

    fun on(target: EventTarget, type: String, listener: ((Event) -> Unit)?) {
        if (listener == null) return

        // wrap the listener so we won't have crazy exception strings all around
        // this also helps with removal as the object will be the same as we added

        addWrapped(target, type) { event ->
            try {
                this.event = event
                listener(event)
            } catch (ex: Throwable) {
                onException(ex)
            } finally {
                this.event = null
            }
        }

    }

    /**
     * Add a wrapped event listener. Keep it private, so users won't start to use it.
     */
    private fun addWrapped(target: EventTarget, type: String, wrapper: ((Event) -> Unit)) {

        eventListeners.add(type to (target to wrapper))

        // sanity check, this means that some code adds listeners again and again

        if (eventListeners.size > 100) {
            window.alert("Internal program error (more than 100 listeners) in ${this::class.simpleName}")
        }

        // finally, we are ready to add the listener to the HTML element

        target.addEventListener(type, wrapper)

    }

    fun off(typeToRemove: String) {
        eventListeners.forEach { (type, entry) ->
            if (type == typeToRemove) {
                entry.first.removeEventListener(type, entry.second)
            }
        }
    }

    private fun cleanupEventListeners() {
        if (::eventListeners.isInitialized) {
            eventListeners.forEach { (type, entry) ->
                entry.first.removeEventListener(type, entry.second)
            }
        }
    }

    open fun onException(ex: Throwable) = log(ex)

    // ----  Builder  --------

    private fun runBuild(e: HTMLElement, className: String?, build: ZkElement.() -> Unit) {
        if (className != null) e.classList.add(className)
        val original = currentElement
        currentElement = e
        this.build()
        currentElement = original
    }

    /**
     * Convenience to set the style of the current [currentElement].
     */
    fun style(styleBuilder: CSSStyleDeclaration.() -> Unit) {
        with(currentElement.style) {
            styleBuilder()
        }
    }

    /**
     * Creates "div" [HTMLElement] and executes the builder function on it.
     *
     * @param  className  CSS class to add. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun div(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates a "div" [HTMLElement] with zkClasses.grid added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun row(className: String? = null, build: ZkElement.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += zkClasses.row
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates "div" [HTMLElement] with zkClasses.column added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun column(className: String? = null, build: ZkElement.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += zkClasses.column
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates a "div" [HTMLElement] with zkClasses.row added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  style      Inline style for the grid. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    fun grid(className: String? = null, style: String? = null, build: ZkElement.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += zkClasses.grid
        if (style != null) e.style.cssText = style
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
     * Creates an IMG with a source url. Use [currentElement] to add attributes other than the source.
     *
     * ```
     *
     *     with(buildContext as HTMLImageElement) {
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
    fun image(src: String, className: String? = null, build: ZkElement.() -> Unit = { }): HTMLImageElement {
        val img = document.createElement("img") as HTMLImageElement
        img.src = src
        runBuild(img, className, build)
        return img
    }

    fun table(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableElement {
        val e = document.createElement("table") as HTMLTableElement
        runBuild(e, className, build)
        return e
    }

    fun tr(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableRowElement {
        val e = document.createElement("tr") as HTMLTableRowElement
        runBuild(e, className, build)
        return e
    }

    fun td(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableCellElement {
        val e = document.createElement("td") as HTMLTableCellElement
        runBuild(e, className, build)
        return e
    }

    @PublicApi
    fun thead(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableSectionElement {
        val e = document.createElement("thead") as HTMLTableSectionElement
        runBuild(e, className, build)
        return e
    }

    @PublicApi
    fun tbody(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableSectionElement {
        val e = document.createElement("tbody") as HTMLTableSectionElement
        runBuild(e, className, build)
        return e
    }

    @PublicApi
    fun th(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableCellElement {
        val e = document.createElement("th") as HTMLTableCellElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates an unnamed [ZkElement].
     */
    fun zke(className: String? = null, build: ZkElement.() -> Unit = { }): ZkElement {
        val e = ZkElement()
        if (className != null) e.className = className
        currentElement.appendChild(e.element)
        childElements.plusAssign(e)
        e.build()
        return e
    }

    /**
     * Creates a SPAN with [HTMLElement.innerHTML] set to the string passed.
     * The string is **not escaped**. You have to escape it yourself.
     */
    operator fun String.not(): HTMLElement {
        val e = document.createElement("span")
        e.innerHTML = this
        currentElement.append(e)
        return e as HTMLElement
    }

    /**
     * Creates a text node using [HTMLElement.appendText] with the string passed as content.
     * No need for escape.
     */
    operator fun String.unaryPlus(): Element {
        return currentElement.appendText(this)
    }

    /**
     * Creates a text node using [HTMLElement.appendText] with the string passed as content.
     * No need for escape.
     */
    operator fun String?.unaryPlus(): Element? {
        if (this == null) return null
        return currentElement.appendText(this)
    }

    /**
     * Adds an HTML element.
     */
    operator fun HTMLElement.unaryPlus(): HTMLElement {
        currentElement.appendChild(this)
        return this
    }

    /**
     * Adds a [ZkElement].
     */
    operator fun ZkElement.unaryPlus(): ZkElement {
        this@ZkElement.currentElement.appendChild(this.element)
        this@ZkElement.childElements += this.init()
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
     * @param  build      The builder function to build the content of the div. Optional.
     */
    infix fun HTMLElement.build(build: ZkElement.() -> Unit): HTMLElement {
        runBuild(this, className, build)
        return this
    }

    /**
     * Hide an [HTMLElement] by adding zkClasses.hidden to its class list.
     * That is "display: none !imporant".
     */
    fun HTMLElement.hide(): HTMLElement {
        this.classList += zkClasses.hidden
        return this
    }

    /**
     * Show an [HTMLElement] by removing zkClasses.hidden from its class list.
     */
    fun HTMLElement.show(): HTMLElement {
        this.classList -= zkClasses.hidden
        return this
    }

    // ----  Executor permissions, loggged in etc  --------

    /**
     * Execute the builder function when the user **is not** the
     * anonymous user, i.e. is a logged in user.
     */
    @PublicApi
    fun ifNotAnonymous(builder: ZkElement.() -> Unit) {
        if (Application.executor.anonymous) return
        this.builder()
    }

    /**
     * Execute the builder function when the user **is the
     * anonymous user, i.e. is **not** a logged in user.
     */
    @PublicApi
    fun ifAnonymous(builder: ZkElement.() -> Unit) {
        if (Application.executor.anonymous) return
        this.builder()
    }

    /**
     * Execute the builder function when the user **has**
     * the given role.
     */
    @PublicApi
    fun withRole(role: String, builder: ZkElement.() -> Unit) {
        if (role !in Application.executor.roles) return
        this.builder()
    }

    /**
     * Execute the builder function when the user **does not have**
     * the given role.
     */
    @PublicApi
    fun withoutRole(role: String, builder: ZkElement.() -> Unit) {
        if (role in Application.executor.roles) return
        this.builder()
    }

}
