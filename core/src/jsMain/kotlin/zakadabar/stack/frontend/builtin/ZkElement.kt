/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin

import kotlinx.browser.document
import kotlinx.dom.appendText
import kotlinx.dom.clear
import org.w3c.dom.*
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.resources.css.stringOrPx
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.minusAssign
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass

/**
 * A base class for UI elements.
 *
 * @property  id               ID of [element]. All ZkElements have a unique, auto-generated id.
 *
 * @property  element          The HTML DOM node that belongs to this ZkElement. The id of this
 *                             DOM node is "zk-$id" where id is the value of the [id] property.
 *
 * @property  lifeCycleState  State of the ZkElement. Used to prevent un-intentional reuse of
 *                             elements and to manage element swapping.
 *
 * @property  childElements    ZkElements that are child of this one. Child management functions
 *                             are there to manage adding / removing children easy.
 *
 * @property  buildElement     The HTML DOM element that is currently built. This may change a lot
 *                             during the build process as DIVs are usually added to build a
 *                             visual layout.
 */
open class ZkElement(
    val element: HTMLElement = document.createElement("div") as HTMLElement
) {

    companion object {

        var nextId: Long = 1L
            get() = field ++

        /**
         * When true a "klass" data set attribute will be added to [element]. This
         * attribute contains the class name of the ZkElement, useful for debugging.
         */
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
        @PublicApi
        fun buildNew(builder: ZkElement.() -> Unit) = ZkElement().build(builder)

        val h100 = zkLayoutStyles.h100

        val w100 = zkLayoutStyles.w100
    }

    val id = nextId

    var buildElement = element

    var lifeCycleState = ZkElementState.Initialized

    init {
        element.id = "zk-$id"
        if (addKClass) element.dataset["kclass"] = this::class.simpleName.toString()
    }

    val childElements by lazy { mutableListOf<ZkElement>() }

    // -------------------------------------------------------------------------
    //   Shorthands variables
    // -------------------------------------------------------------------------

    /**
     * Display name of the current user.
     */
    val displayName
        get() = ZkApplication.executor.account.displayName

    var gridTemplateRows: String
        get() = buildElement.style.getPropertyValue("grid-template-rows")
        set(value) {
            buildElement.style.setProperty("grid-template-rows", value)
        }

    var gridTemplateColumns: String
        get() = buildElement.style.getPropertyValue("grid-template-columns")
        set(value) {
            buildElement.style.setProperty("grid-template-columns", value)
        }

    var gridGap: Any
        get() = buildElement.style.getPropertyValue("grid-gap")
        set(value) {
            buildElement.style.setProperty("grid-gap", stringOrPx(value))
        }

    // -------------------------------------------------------------------------
    //   Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Called when the element is created, use this for initial setup.
     */
    open fun onCreate() {
        // Life cycle state is set by "resume". The reasoning is that I want to
        // avoid the necessity to add super.onCreate() to all overrides. This
        // method is called only from "resume" and I think there wont' be many
        // other places.
    }

    /**
     * Called when the this element is resumed. The default implementation
     * calls [onResume] of all child elements and then sets the state of this
     * elements to [ZkElementState.Resumed].
     *
     * Call of this method usually means that the element is put screen,
     * however, it may be hidden.
     *
     * This method is called after the [element] of the child is added to
     * the HTML DOM and after it is added to [childElements].
     */
    open fun onResume() {
        childElements.forEach { child ->
            child.onResume()
        }
        lifeCycleState = ZkElementState.Resumed
    }

    /**
     * Called when the element is paused. The default implementation
     * calls [onPause] of all child elements and then sets the state of this
     * elements to [ZkElementState.Created].
     *
     * Call of this method usually means that the element is remove from the screen.
     */
    open fun onPause() {
        childElements.forEach { child ->
            child.onPause()
        }
        lifeCycleState = ZkElementState.Created
    }

    /**
     * Called when the element is destroyed and will never be used again.
     *
     * As of now this happens only when [clearChildren] is called. Removing
     * an element with [minusAssign] just pauses the element it does not
     * destroy it.
     *
     * The reason for this behaviour is that the browser performs garbage
     * collection and elements which are not referenced will be removed
     * by that process automatically.
     *
     * I am actually not sure if we want this method.
     */
    open fun onDestroy() {
        clearChildren()
        element.clear()
        lifeCycleState = ZkElementState.Destroyed
    }

    // -------------------------------------------------------------------------
    //   Shorthands for DOM data
    // -------------------------------------------------------------------------

    /**
     * Shorthand for the innerHTML property of the [buildElement].
     */
    var innerHTML: String
        inline get() = buildElement.innerHTML
        inline set(value) {
            element.innerHTML = value
        }

    /**
     * Shorthand for the innerText property of the [buildElement].
     */
    var innerText: String
        inline get() = buildElement.innerText
        inline set(value) {
            element.innerText = value
        }

    /**
     * Shorthand for the className property of the [buildElement].
     */
    var className: String
        inline get() = buildElement.className
        inline set(value) {
            element.className = value
        }

    /**
     * Shorthand for the setting the style string of the [buildElement].
     */
    var style: String
        inline get() = buildElement.style.cssText
        inline set(value) {
            buildElement.style.cssText = value
        }

    /**
     * Shorthand for the classList property of the HTML [element].
     */
    val classList: DOMTokenList
        inline get() = buildElement.classList

    // -------------------------------------------------------------------------
    //   Convenience functions to call build
    // -------------------------------------------------------------------------

    /**
     * Calls [builder] and returns with this element.
     */
    open infix fun build(builder: ZkElement.() -> Unit): ZkElement {
        this.builder()
        return this
    }

    /**
     * Calls [builder] in a launched coroutine and returns with this element.
     * [builder] may call suspending routines.
     */
    open infix fun launchBuild(builder: suspend ZkElement.() -> Unit): ZkElement {
        io {
            this.builder()
        }
        return this
    }

    // -------------------------------------------------------------------------
    //   DOM and CSS utilities
    // -------------------------------------------------------------------------

    /**
     * True when the DOM node does not have the [zkLayoutStyles].hidden class in
     * it's class list, false otherwise.
     */
    fun isShown() = ! element.classList.contains(zkLayoutStyles.hidden)

    /**
     * True when the DOM node has the [zkLayoutStyles].hidden class in it's class list,
     * false otherwise.
     */
    fun isHidden() = element.classList.contains(zkLayoutStyles.hidden)

    /**
     * Toggles the [zkLayoutStyles].hidden class in the DOM node's class list.
     */
    fun toggle() = element.classList.toggle(zkLayoutStyles.hidden)

    /**
     * Hides the DOM node by adding [zkLayoutStyles].hidden CSS class to the DOM
     * node's class list.
     */
    open fun hide(): ZkElement {
        classList.add(zkLayoutStyles.hidden)
        return this
    }

    /**
     * Shows the DOM node by removing [zkLayoutStyles].hidden CSS class from the DOM
     * node's class list.
     */
    open fun show(): ZkElement {
        classList.remove(zkLayoutStyles.hidden)
        return this
    }

    /**
     * Calls the DOM node's focus method to focus on the element. Don't forget
     * to set tabIndex when needed.
     */
    open fun focus(): ZkElement {
        element.focus()
        return this
    }

    /**
     * Clears the children and the HTML node of this element. Childrens are paused
     * and then destroyed.
     */
    open fun clear() {
        clearChildren() // pauses and destroys all children
        element.clear() // clears the DOM node
    }

    /**
     * Adds a right margin to the element.
     */
    infix fun marginRight(size: Any): ZkElement {
        element.style.marginRight = if (size is Int) "${size}px" else size.toString()
        return this
    }

    /**
     * Adds a left margin to the element.
     */
    infix fun marginLeft(size: Any): ZkElement {
        element.style.marginLeft = if (size is Int) "${size}px" else size.toString()
        return this
    }

    /**
     * Adds a bottom margin to the element.
     */
    infix fun marginBottom(size: Any): ZkElement {
        element.style.marginBottom = if (size is Int) "${size}px" else size.toString()
        return this
    }

    /**
     * Adds [className] to the CSS class list of the DOM node.
     */
    infix fun css(className: String): ZkElement {
        element.classList.add(className)
        return this
    }

    /**
     * Adds [classNames] to the CSS class list of the DOM node.
     */
    fun css(vararg classNames: String): ZkElement {
        element.classList.add(*classNames)
        return this
    }

    /**
     * Adds [className] to the CSS class list of the DOM node if the class list
     * of the DOM node is empty.
     */
    fun withOptCss(className: String): ZkElement {
        if (element.className.isBlank()) {
            element.className = className
        }
        return this
    }

    /**
     * Sets the "grid-row" CSS property.
     */
    infix fun gridRow(value: String): ZkElement {
        element.style.setProperty("grid-row", value)
        return this
    }

    /**
     * Sets the "grid-row" CSS property.
     */
    infix fun gridRow(value: Int): ZkElement {
        element.style.setProperty("grid-row", value.toString())
        return this
    }

    /**
     * Sets the "grid-column" CSS property.
     */
    infix fun gridColumn(value: String): ZkElement {
        element.style.setProperty("grid-column", value)
        return this
    }

    /**
     * Sets the "grid-column" CSS property.
     */
    infix fun gridColumn(value: Int): ZkElement {
        element.style.setProperty("grid-column", value.toString())
        return this
    }

    /**
     * Appends the given class to the class list of the element.
     *
     * @param  className  Name of the class to append.
     */
    infix fun Element.css(className: String): Element {
        this.classList += className
        return this
    }

    /**
     * Append the given class to the class list of the element.
     *
     * @param  className  Name of the class to append.
     */
    infix fun HTMLElement.css(className: String): HTMLElement {
        this.classList += className
        return this
    }

    /**
     * Sets the "grid-row" CSS property.
     */
    infix fun HTMLElement.gridRow(value: String): HTMLElement {
        style.setProperty("grid-row", value)
        return this
    }

    /**
     * Sets the "grid-row" CSS property.
     */
    infix fun HTMLElement.gridRow(value: Int): HTMLElement {
        style.setProperty("grid-row", value.toString())
        return this
    }

    /**
     * Sets the "grid-column" CSS property.
     */
    infix fun HTMLElement.gridColumn(value: String): HTMLElement {
        style.setProperty("grid-column", value)
        return this
    }

    /**
     * Sets the "grid-column" CSS property.
     */
    infix fun HTMLElement.gridColumn(value: Int): HTMLElement {
        style.setProperty("grid-column", value.toString())
        return this
    }

    // -------------------------------------------------------------------------
    //   Child elements
    // -------------------------------------------------------------------------

    /**
     * Synchronizes the state of the children with the state of this element.
     */
    open fun syncChildrenState(child: ZkElement) {
        when (lifeCycleState) {

            ZkElementState.Initialized, ZkElementState.Created -> {
                when (child.lifeCycleState) {
                    ZkElementState.Initialized -> {
                        child.onCreate()
                        child.lifeCycleState = ZkElementState.Created
                    }
                    ZkElementState.Created -> Unit
                    ZkElementState.Resumed, ZkElementState.Destroyed -> {
                        throw IllegalStateException("invalid element state ${child.lifeCycleState} for ${child::class.simpleName}")
                    }
                }
            }

            ZkElementState.Resumed -> {
                when (child.lifeCycleState) {
                    ZkElementState.Initialized -> {
                        child.onCreate()
                        child.lifeCycleState = ZkElementState.Created
                        child.onResume()
                        child.lifeCycleState = ZkElementState.Resumed
                    }
                    ZkElementState.Created -> {
                        child.onResume()
                        child.lifeCycleState = ZkElementState.Resumed
                    }
                    ZkElementState.Resumed, ZkElementState.Destroyed -> {
                        throw IllegalStateException("invalid element state ${child.lifeCycleState} for ${child::class.simpleName}")
                    }
                }
            }

            ZkElementState.Destroyed -> {
                throw IllegalStateException("invalid element state ${child.lifeCycleState} for ${this::class.simpleName}")
            }
        }
    }

    /**
     * Adds all children from [children].
     */
    operator fun plusAssign(children: List<ZkElement>) {
        children.forEach { child ->
            this += child
        }
    }

    /**
     * Adds a child element. When [child] is null this is a no-op.
     * The child must be in [ZkElementState.Created] state.
     */
    open operator fun plusAssign(child: ZkElement?) {
        if (child == null) return

        this.element.appendChild(child.element)
        childElements += child

        syncChildrenState(child)
    }

    /**
     * Adds a child element to children and synchronizes its state.
     *
     * The child is not added to the DOM.
     *
     * When [child] is null this is a no-op.
     *
     * The child must be in [ZkElementState.Created] state.
     */
    open fun addChildSkipDOM(child: ZkElement?) {
        if (child == null) return
        childElements.add(0, child)
        syncChildrenState(child)
    }

    /**
     * Adds a child element. When [child] is null this is a no-op.
     * The child must be in [ZkElementState.Created] state.
     *
     * The child is added before the current first child.
     */
    open infix fun insertFirst(child: ZkElement?) {
        if (child == null) return

        this.element.insertBefore(child.element, this.element.firstChild)
        childElements.add(0, child)

        syncChildrenState(child)
    }

    /**
     * Adds a child element. When [child] is null this is a no-op.
     * The child must be in [ZkElementState.Created] state.
     *
     * The child is added after the child passed in [after].
     * When [after] is null the [insertFirst] is called.
     */
    open fun insertAfter(child: ZkElement?, after: ZkElement?) {
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
                    childElements.add(index + 1, child)
                    syncChildrenState(child)
                }
            }
        }
    }

    /**
     * Adds a child element. When [child] is null this is a no-op.
     * The child must be in [ZkElementState.Created] state.
     *
     * The child is added before the child passed in [before].
     * When [before] is null the [child] is added to the end.
     */
    open fun insertBefore(child: ZkElement?, before: ZkElement?) {
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
            childElements.add(index, child)

            syncChildrenState(child)
        }
    }

    /**
     * Remove the [child]. Only pauses the child, does not destroy it. The DOM node
     * is removed from the page DOM.
     */
    open operator fun minusAssign(child: ZkElement?) {
        if (child == null) return

        childElements -= child
        child.element.remove()

        child.onPause()

        child.lifeCycleState = ZkElementState.Created
    }

    /**
     * Remove all children that are of the given class.
     */
    open operator fun minusAssign(clazz: KClass<*>) {
        childElements.filter { clazz.isInstance(it) }.forEach {
            this -= it
        }
    }

    /**
     * Remove and destroy all children. Calls onPause and then onDestroy for
     * each child.
     */
    open fun clearChildren(): ZkElement {
        if (::childElements.isInitialized) {
            childElements.forEach { child ->
                this -= child
                child.onDestroy()
            }
        }
        return this
    }

    /**
     * Check if this element has at least one child element of the given class.
     */
    fun hasChildOf(clazz: KClass<*>): Boolean {
        for (child in childElements) {
            if (clazz.isInstance(child)) return true
        }
        return false
    }

    /**
     * Get a child [ZkElement] of the given class. Useful in event handlers
     * to get child element without storing it in a variable.
     */
    inline operator fun <reified T : ZkElement> get(kClass: KClass<T>): T {
        return childElements.first { kClass.isInstance(it) } as T
    }

    /**
     * Get the first [ZkElement] by the given CSS class.
     */
    inline operator fun <reified T : ZkElement> get(cssClassName: String): T {
        return childElements.first { it.classList.contains(cssClassName) } as T
    }

    inline fun <reified T : ZkElement> find(): List<T> {
        val kClass = T::class
        @Suppress("UNCHECKED_CAST") // checking for class
        return childElements.filter { kClass.isInstance(it) } as List<T>
    }

    inline fun <reified T : ZkElement> findFirst(): T {
        val kClass = T::class
        @Suppress("UNCHECKED_CAST") // checking for class
        return childElements.first { kClass.isInstance(it) } as T
    }

    // -------------------------------------------------------------------------
    //   Event listeners
    // -------------------------------------------------------------------------

    /**
     * Attach a DOM event handler to this element.
     */
    fun on(type: String, listener: ((Event) -> Unit)?) = on(element, type, listener)

    /**
     * Attach a DOM event handler to the given DOM target node.
     */
    fun on(target: EventTarget, type: String, listener: ((Event) -> Unit)?) {
        if (listener == null) return
        target.addEventListener(type, listener)
    }

    // -------------------------------------------------------------------------
    //   Builder
    // -------------------------------------------------------------------------

    private fun runBuild(e: HTMLElement, className: String?, build: ZkElement.() -> Unit) {
        if (className != null) e.classList.add(className)
        val original = buildElement
        buildElement = e
        this.build()
        buildElement = original
    }

    /**
     * Convenience to set the style of the current [buildElement].
     */
    open fun style(styleBuilder: CSSStyleDeclaration.() -> Unit) {
        with(buildElement.style) {
            styleBuilder()
        }
    }

    /**
     * Creates "span" [HTMLElement] and executes the builder function on it.
     *
     * @param  className  CSS class to add. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun span(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("span") as HTMLElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates "div" [HTMLElement] and executes the builder function on it.
     *
     * @param  className  CSS class to add. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun div(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates "div" [HTMLElement] and executes the builder function on it.
     *
     * @param  classNames  CSS class names to add. Optional.
     * @param  build       The builder function to build the content of the div. Optional.
     */
    open fun div(vararg classNames: String, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        for (className in classNames) {
            e.classList.add(className)
        }
        runBuild(e, null, build)
        return e
    }

    /**
     * Creates a "div" [HTMLElement] with ZkClasses.grid added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun row(className: String? = null, build: ZkElement.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += zkLayoutStyles.row
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates "div" [HTMLElement] with ZkClasses.column added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun column(className: String? = null, build: ZkElement.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += zkLayoutStyles.column
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates a "div" [HTMLElement] with ZkClasses.row added and executes the builder on it.
     *
     * @param  className  Additional CSS class. Optional.
     * @param  style      Inline style for the grid. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun grid(className: String? = null, style: String? = null, build: ZkElement.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += zkLayoutStyles.grid
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
    open fun gap(width: Any = "100%", height: Any = "100%"): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.style.height = if (height is Int) "${height}px" else height.toString()
        e.style.width = if (width is Int) "${width}px" else width.toString()
        return e
    }

    /**
     * Creates an IMG with a source url. Use [buildElement] to add attributes other than the source.
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
    open fun image(src: String, className: String? = null, build: ZkElement.() -> Unit = { }): HTMLImageElement {
        val img = document.createElement("img") as HTMLImageElement
        img.src = src
        runBuild(img, className, build)
        return img
    }

    /**
     * Creates an [HTMLTableElement] and runs the builder on it.
     */
    open fun table(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableElement {
        val e = document.createElement("table") as HTMLTableElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates an [HTMLTableRowElement] and runs the builder on it.
     */
    open fun tr(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableRowElement {
        val e = document.createElement("tr") as HTMLTableRowElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates an [HTMLTableCellElement] and runs the builder on it.
     */
    open fun td(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableCellElement {
        val e = document.createElement("td") as HTMLTableCellElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates a "thead" and runs the builder on it.
     */
    open fun thead(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableSectionElement {
        val e = document.createElement("thead") as HTMLTableSectionElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates a "tbody" and runs the builder on it.
     */
    open fun tbody(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableSectionElement {
        val e = document.createElement("tbody") as HTMLTableSectionElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates a "th" and runs the builder on it.
     */
    open fun th(className: String? = null, build: ZkElement.() -> Unit = { }): HTMLTableCellElement {
        val e = document.createElement("th") as HTMLTableCellElement
        runBuild(e, className, build)
        return e
    }

    /**
     * Creates an unnamed [ZkElement].
     */
    open fun zke(className: String? = null, build: ZkElement.() -> Unit = { }): ZkElement {
        val e = ZkElement()
        if (className != null) e.className = className
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
        buildElement.append(e)
        return e as HTMLElement
    }

    /**
     * Creates a text node using [HTMLElement.appendText] with the string passed as content.
     * The text is added with [appendText], therefore no need to escape the text.
     */
    operator fun String.unaryPlus(): Element {
        return buildElement.appendText(this)
    }

    /**
     * Creates a text node using [HTMLElement.appendText] with the string passed as content.
     * The text is added with [appendText], therefore no need to escape the text.
     */
    operator fun String?.unaryPlus(): Element? {
        if (this == null) return null
        return buildElement.appendText(this)
    }

    /**
     * Adds an HTML element.
     */
    operator fun HTMLElement.unaryPlus(): HTMLElement {
        buildElement.appendChild(this)
        return this
    }

    /**
     * Adds a [ZkElement] as a child.
     */
    operator fun ZkElement.unaryPlus(): ZkElement {
        this@ZkElement.buildElement.appendChild(this.element)
        this@ZkElement.childElements += this
        this@ZkElement.syncChildrenState(this)
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
     * Hide an [HTMLElement] by adding ZkLayoutStyles.hidden to its class list.
     * That is "display: none !imporant".
     */
    fun HTMLElement.hide(): HTMLElement {
        this.classList += zkLayoutStyles.hidden
        return this
    }

    /**
     * Show an [HTMLElement] by removing ZkLayoutStyles.hidden from its class list.
     */
    fun HTMLElement.show(): HTMLElement {
        this.classList -= zkLayoutStyles.hidden
        return this
    }

    // -------------------------------------------------------------------------
    //   Executor permissions, logged in etc
    // -------------------------------------------------------------------------

    /**
     * Execute the builder function when the user **is not** the
     * anonymous user, i.e. is a logged in user.
     */
    @PublicApi
    fun ifNotAnonymous(builder: ZkElement.() -> Unit) {
        if (ZkApplication.executor.anonymous) return
        this.builder()
    }

    /**
     * Execute the builder function when the user **is the
     * anonymous user, i.e. is **not** a logged in user.
     */
    @PublicApi
    fun ifAnonymous(builder: ZkElement.() -> Unit) {
        if (! ZkApplication.executor.anonymous) return
        this.builder()
    }

    /**
     * Execute the builder function when the user **has**
     * the given role.
     */
    @PublicApi
    fun withRole(role: String, builder: ZkElement.() -> Unit) {
        if (role !in ZkApplication.executor.roles) return
        this.builder()
    }

    /**
     * Execute the builder function when the user **has**
     * the given role.
     */
    @PublicApi
    fun withOneOfRoles(vararg roles: String, builder: ZkElement.() -> Unit) {
        roles.forEach {
            if (it in ZkApplication.executor.roles) {
                this.builder()
                return
            }
        }
    }

    /**
     * Execute the builder function when the user **does not have**
     * the given role.
     */
    @PublicApi
    fun withoutRole(role: String, builder: ZkElement.() -> Unit) {
        if (role in ZkApplication.executor.roles) return
        this.builder()
    }

}
