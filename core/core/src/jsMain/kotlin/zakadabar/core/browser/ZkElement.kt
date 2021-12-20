/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser

import kotlinx.browser.document
import kotlinx.dom.appendText
import kotlinx.dom.clear
import org.w3c.dom.*
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget
import zakadabar.core.browser.application.application
import zakadabar.core.browser.application.executor
import zakadabar.core.browser.dock.ZkDockedElement
import zakadabar.core.browser.dock.ZkDockedElementState
import zakadabar.core.browser.field.ZkFieldBase
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkIconSource
import zakadabar.core.resource.css.CssValueConst
import zakadabar.core.resource.css.ZkCssStyleRule
import zakadabar.core.resource.css.percent
import zakadabar.core.resource.localizedStrings
import zakadabar.core.util.PublicApi
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
 * @property  buildPoint       The HTML DOM element that is currently built. This may change a lot
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
        @Deprecated("EOL: 2021.8.1  -  use zke { io { ... } } instead", ReplaceWith("zke"))
        fun launchBuildNew(builder: suspend ZkElement.() -> Unit) = ZkElement().launchBuild(builder)

        /**
         * Creates an anonymous [ZkElement] and calls [ZkElement.build] on it with
         * the [builder] function.
         *
         * Use this function when there is no need to asynchronous data fetch.
         */
        @Deprecated("EOL: 2021.8.1  -  use zke { ... } instead", ReplaceWith("zke"))
        fun buildNew(builder: ZkElement.() -> Unit) = ZkElement().build(builder)

        @PublicApi
        val h100 = zkLayoutStyles.h100

        @PublicApi
        val w100 = zkLayoutStyles.w100
    }

    val id = nextId

    var buildPoint = element

    var lifeCycleState = ZkElementState.Initialized

    init {
        element.id = "zk-$id"
        if (addKClass) element.dataset["kclass"] = this::class.simpleName.toString()
    }

    val childElements = mutableListOf<ZkElement>()

    // -------------------------------------------------------------------------
    //   Shorthands variables
    // -------------------------------------------------------------------------

    var gridAutoRows: String
        get() = buildPoint.style.getPropertyValue("grid-auto-rows")
        set(value) {
            buildPoint.style.setProperty("grid-auto-rows", value)
        }

    var gridAutoColumns: String
        get() = buildPoint.style.getPropertyValue("grid-auto-columns")
        set(value) {
            buildPoint.style.setProperty("grid-auto-columns", value)
        }

    var gridTemplateRows: String
        get() = buildPoint.style.getPropertyValue("grid-template-rows")
        set(value) {
            buildPoint.style.setProperty("grid-template-rows", value)
        }

    var gridTemplateColumns: String
        get() = buildPoint.style.getPropertyValue("grid-template-columns")
        set(value) {
            buildPoint.style.setProperty("grid-template-columns", value)
        }

    var gridGap: String
        get() = buildPoint.style.getPropertyValue("grid-gap")
        set(value) {
            buildPoint.style.setProperty("grid-gap", value)
        }

    var height: String
        get() = buildPoint.style.height
        set(value) {
            buildPoint.style.setProperty("height", value)
        }

    var width: String
        get() = buildPoint.style.width
        set(value) {
            buildPoint.style.setProperty("width", value)
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
     * Shorthand for the innerHTML property of the [buildPoint].
     */
    var innerHTML: String
        inline get() = buildPoint.innerHTML
        inline set(value) {
            buildPoint.innerHTML = value
        }

    /**
     * Shorthand for the innerText property of the [buildPoint].
     */
    var innerText: String
        inline get() = buildPoint.innerText
        inline set(value) {
            buildPoint.innerText = value
        }

    /**
     * Shorthand for the className property of the [buildPoint].
     */
    var className: String
        inline get() = buildPoint.className
        inline set(value) {
            buildPoint.className = value
        }

    /**
     * Shorthand for the setting the style string of the [buildPoint].
     */
    var style: String
        inline get() = buildPoint.style.cssText
        inline set(value) {
            buildPoint.style.cssText = value
        }

    /**
     * Shorthand for the classList property of the HTML [element].
     */
    val classList: DOMTokenList
        inline get() = buildPoint.classList

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
    fun isShown() = ! element.classList.contains(zkLayoutStyles.hidden.cssClassname)

    /**
     * True when the DOM node has the [zkLayoutStyles].hidden class in it's class list,
     * false otherwise.
     */
    fun isHidden() = element.classList.contains(zkLayoutStyles.hidden.cssClassname)

    /**
     * Toggles the [zkLayoutStyles].hidden class in the DOM node's class list.
     */
    fun toggle() = element.classList.toggle(zkLayoutStyles.hidden.cssClassname)

    /**
     * Hides the DOM node by adding [zkLayoutStyles].hidden CSS class to the DOM
     * node's class list.
     */
    open fun hide(): ZkElement {
        classList.add(zkLayoutStyles.hidden.cssClassname)
        return this
    }

    /**
     * Shows the DOM node by removing [zkLayoutStyles].hidden CSS class from the DOM
     * node's class list.
     */
    open fun show(): ZkElement {
        classList.remove(zkLayoutStyles.hidden.cssClassname)
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
     * Adds [className] to the CSS class list of [element].
     */
    infix fun css(className: String): ZkElement {
        element.classList.add(className)
        return this
    }

    /**
     * Adds the rule to the CSS class list of [element].
     */
    infix fun css(rule: ZkCssStyleRule): ZkElement {
        element.classList += rule
        return this
    }

    /**
     * Adds [classNames] to the CSS class list of [element].
     */
    fun css(vararg classNames: String): ZkElement {
        element.classList.add(*classNames)
        return this
    }

//    /**
//     * Adds the rules to the CSS class list of [element].
//     */
//    fun css(vararg rules: ZkCssStyleRule): ZkElement {
//        element.classList.plusAssign(*rules)
//        return this
//    }

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
     * Appends the given class to the class list of the build point.
     */
    operator fun ZkCssStyleRule.unaryPlus() {
        buildPoint.classList += this
    }

    /**
     * Removes the given class to the class list from the build point.
     */
    operator fun ZkCssStyleRule.unaryMinus() {
        buildPoint.classList -= this
    }

    /**
     * Removes all current CSS classes and adds this one (at buid point).
     */
    operator fun ZkCssStyleRule.not() {
        buildPoint.className = this.cssClassname
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
     * Append the given class to the class list of the element.
     *
     * @param  rule  Name of the class to append.
     */
    infix fun HTMLElement.css(rule: ZkCssStyleRule): HTMLElement {
        this.classList += rule
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
            when (val index = childElements.indexOf(after)) {
                - 1 -> plusAssign(child)
                childElements.lastIndex -> plusAssign(child)
                else -> {
                    val insertPoint = childElements[index].element.nextElementSibling
                    insertPoint?.parentElement?.insertBefore(child.element, insertPoint)
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
            before.element.parentElement?.insertBefore(child.element, childElements[index].element)
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
        childElements.forEach { child ->
            child.onDestroy()
        }
        childElements.clear()
        return this
    }

    /**
     * True when this [ZkElement] has no children an [element].innerHTML is empty.
     */
    fun isEmpty(): Boolean {
        return (childElements.isEmpty() && element.innerHTML.isEmpty())
    }

    /**
     * Check if this element has at least one child element of the given class.
     */
    inline fun <reified T : ZkElement> hasChildOf(): Boolean {
        val kClass = T::class
        for (child in childElements) {
            if (kClass.isInstance(child)) return true
        }
        return false
    }

    /**
     * Get a child [ZkElement] of the given class. Useful in event handlers
     * to get child element without storing it in a variable.
     *
     * @throws NoSuchElementException
     */
    inline operator fun <reified T : ZkElement> get(kClass: KClass<T>): T {
        return childElements.first { kClass.isInstance(it) } as T
    }

    /**
     * Get the first [ZkElement] child by the given CSS class.
     *
     * @throws NoSuchElementException
     */
    inline operator fun <reified T : ZkElement> get(cssClassName: String): T {
        return childElements.first { it.classList.contains(cssClassName) } as T
    }

    /**
     * Get the first [ZkElement] child by the given CSS class.
     *
     * @throws NoSuchElementException
     */
    inline operator fun <reified T : ZkElement> get(rule: ZkCssStyleRule): T {
        return childElements.first { it.classList.contains(rule.cssClassname) } as T
    }

    /**
     * Get all [ZkElement] children of the given Kotlin class.
     */
    inline fun <reified T : ZkElement> find(): List<T> {
        val kClass = T::class
        @Suppress("UNCHECKED_CAST") // checking for class
        return childElements.filter { kClass.isInstance(it) } as List<T>
    }

    @Deprecated("EOL: 2021.8.1  -  use first instead", ReplaceWith("first<T>()"))
    inline fun <reified T : ZkElement> findFirst(): T {
        val kClass = T::class
        @Suppress("UNCHECKED_CAST") // checking for class
        return childElements.first { kClass.isInstance(it) } as T
    }

    /**
     * Get the first [ZkElement] child of the given Kotlin class.
     *
     * @throws NoSuchElementException
     */
    inline fun <reified T : ZkElement> first(): T {
        val kClass = T::class
        @Suppress("UNCHECKED_CAST") // checking for class
        return childElements.first { kClass.isInstance(it) } as T
    }

    /**
     * Get the first [ZkElement] child of the given Kotlin class
     * returns null when there is no such element.
     */
    inline fun <reified T : ZkElement> firstOrNull(): T? {
        val kClass = T::class
        @Suppress("UNCHECKED_CAST") // checking for class
        return childElements.firstOrNull { kClass.isInstance(it) } as? T
    }

    // -------------------------------------------------------------------------
    //   Event listeners
    // -------------------------------------------------------------------------

    /**
     * Attach a DOM event handler to this element.
     */
    fun on(type: String, listener: ((Event) -> Unit)?) = on(buildPoint, type, listener)

    /**
     * Attach a DOM event handler to the given DOM target node.
     */
    fun on(target: EventTarget, type: String, listener: ((Event) -> Unit)?): ZkElement {
        if (listener == null) return this
        target.addEventListener(type, listener)
        return this
    }

    // -------------------------------------------------------------------------
    //   Builder
    // -------------------------------------------------------------------------

    protected fun runBuild(e: HTMLElement, rule: ZkCssStyleRule?, build: ZkElement.() -> Unit) {
        e.classList += rule
        val original = buildPoint
        buildPoint = e
        this.build()
        buildPoint = original
    }

    /**
     * Convenience to set the style of the current [buildPoint].
     */
    open fun style(styleBuilder: CSSStyleDeclaration.() -> Unit) {
        with(buildPoint.style) {
            styleBuilder()
        }
    }

    /**
     * Set the style at the current build point.
     */
    operator fun CssValueConst.unaryPlus() {
        buildPoint.style.setProperty(this.name, this.value)
    }

    /**
     * Creates "span" [HTMLElement] and executes the builder function on it.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun span(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("span") as HTMLElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates "p" [HTMLElement] and executes the builder function on it.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun p(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("p") as HTMLElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates "div" [HTMLElement] and executes the builder function on it.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun div(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates "div" [HTMLElement] and executes the builder function on it.
     *
     * @param  rules       CSS rules to use. Optional.
     * @param  build       The builder function to build the content of the div. Optional.
     */
    open fun div(vararg rules: ZkCssStyleRule, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        for (rule in rules) {
            e.classList.add(rule.cssClassname)
        }
        runBuild(e, null, build)
        return e
    }

    /**
     * Creates a "div" [HTMLElement]. When [grid] is false, zkLayoutStyles.row class
     * is added. When [grid] is true, zkLayoutStyles.grid is added.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  grid       When true a "grid" is created when false a "flex-box".
     * @param  gap        When [grid] is true and [gap] is true zkLayoutStyles.gridGap is added.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun row(rule: ZkCssStyleRule? = null, grid: Boolean = false, gap: Boolean = true, build: ZkElement.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        if (grid) {
            e.classList += zkLayoutStyles.grid
            if (gap) e.classList += zkLayoutStyles.gridGap
        } else {
            e.classList += zkLayoutStyles.row
        }
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates "div" [HTMLElement] with ZkClasses.column added and executes the builder on it.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun column(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += zkLayoutStyles.column
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates a "div" [HTMLElement] with ZkClasses.row added and executes the builder on it.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  style      Inline style for the grid. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun grid(rule: ZkCssStyleRule? = null, style: String? = null, build: ZkElement.() -> Unit): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.classList += zkLayoutStyles.grid
        if (style != null) e.style.cssText = style
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates "div" [HTMLElement] with height and width passed as parameters.
     *
     * Be careful as Safari (and perhaps Chrome) collapses the gap when you
     * use percent and the parent container is not sized properly.
     *
     * @param  width   Width of the gap. String or Int. Defaults is 100.percent.
     * @param  height  Height of the gap. String or Int. Defaults is 100.percent.
     */
    open fun gap(width: String = 100.percent, height: String = 100.percent): HTMLElement {
        val e = document.createElement("div") as HTMLElement
        e.style.height = height
        e.style.width = width
        return e
    }

    /**
     * Creates "h1" [HTMLElement] and executes the builder function on it.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun h1(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("h1") as HTMLElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates "h2" [HTMLElement] and executes the builder function on it.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun h2(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("h2") as HTMLElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates "h3" [HTMLElement] and executes the builder function on it.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun h3(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("h3") as HTMLElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates "h4" [HTMLElement] and executes the builder function on it.
     *
     * @param  rule       CSS rule to use. Optional.
     * @param  build      The builder function to build the content of the div. Optional.
     */
    open fun h4(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLElement {
        val e = document.createElement("h4") as HTMLElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates an IMG with a source url. Use [buildPoint] to add attributes other than the source.
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
     * @param  rule       CSS rule to use. Optional.
     * @param  build      Builder function to manage the IMG tag added. Optional.
     */
    @PublicApi
    open fun image(src: String, rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLImageElement {
        val img = document.createElement("img") as HTMLImageElement
        img.src = src
        runBuild(img, rule, build)
        return img
    }

    /**
     * Creates an [HTMLTableElement] and runs the builder on it.
     */
    open fun table(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLTableElement {
        val e = document.createElement("table") as HTMLTableElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates an [HTMLTableRowElement] and runs the builder on it.
     */
    open fun tr(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLTableRowElement {
        val e = document.createElement("tr") as HTMLTableRowElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates an [HTMLTableCellElement] and runs the builder on it.
     */
    open fun td(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLTableCellElement {
        val e = document.createElement("td") as HTMLTableCellElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates a "thead" and runs the builder on it.
     */
    open fun thead(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLTableSectionElement {
        val e = document.createElement("thead") as HTMLTableSectionElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates a "tbody" and runs the builder on it.
     */
    open fun tbody(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLTableSectionElement {
        val e = document.createElement("tbody") as HTMLTableSectionElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates a "th" and runs the builder on it.
     */
    open fun th(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): HTMLTableCellElement {
        val e = document.createElement("th") as HTMLTableCellElement
        runBuild(e, rule, build)
        return e
    }

    /**
     * Creates an unnamed [ZkElement].
     */
    open fun zke(rule: ZkCssStyleRule? = null, build: ZkElement.() -> Unit = { }): ZkElement {
        val e = ZkElement()
        e.classList += rule
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
        buildPoint.append(e)
        return e as HTMLElement
    }

    /**
     * Creates a text node using [HTMLElement.appendText] with the string passed as content.
     * The text is added with [appendText], therefore no need to escape the text.
     */
    operator fun String.unaryPlus(): Element {
        return buildPoint.appendText(this)
    }

    /**
     * Creates a text node using [HTMLElement.appendText] with the string passed as content.
     * The text is added with [appendText], therefore no need to escape the text.
     */
    operator fun String?.unaryPlus(): Element? {
        if (this == null) return null
        return buildPoint.appendText(this)
    }

    /**
     * Adds an HTML element at build point.
     */
    operator fun HTMLElement.unaryPlus(): HTMLElement {
        buildPoint.appendChild(this)
        return this
    }

    /**
     * Adds an optional HTML element at build point.
     */
    operator fun HTMLElement?.unaryPlus(): HTMLElement? {
        if (this != null) buildPoint.appendChild(this)
        return this
    }

    /**
     * Adds a [ZkFieldBase] as a child, keeps field type.
     */
    operator fun <T, FT : ZkFieldBase<T, FT>> ZkFieldBase<T, FT>.unaryPlus(): FT {
        this@ZkElement.buildPoint.appendChild(this.element)
        this@ZkElement.childElements += this
        this@ZkElement.syncChildrenState(this)
        @Suppress("UNCHECKED_CAST")
        return this as FT
    }

    /**
     * Adds a [ZkElement] as a child.
     */
    operator fun ZkElement.unaryPlus(): ZkElement {
        this@ZkElement.buildPoint.appendChild(this.element)
        this@ZkElement.childElements += this
        this@ZkElement.syncChildrenState(this)
        return this
    }

    /**
     * Adds a [ZkElement] as a child.
     */
    operator fun ZkElement?.unaryPlus(): ZkElement? {
        if (this == null) return null
        this@ZkElement.buildPoint.appendChild(this.element)
        this@ZkElement.childElements += this
        this@ZkElement.syncChildrenState(this)
        return this
    }

    /**
     * Removes a [ZkElement] child.
     */
    operator fun ZkElement.unaryMinus(): ZkElement {
        this@ZkElement -= this
        return this
    }

    /**
     * Removes a [ZkElement] child.
     */
    operator fun ZkElement?.unaryMinus(): ZkElement? {
        this@ZkElement -= this
        return this
    }

    /**
     * Creates "div" [HTMLElement] and executes the builder function on it.
     *
     * @param  build      The builder function to build the content of the div. Optional.
     */
    infix fun HTMLElement.build(build: ZkElement.() -> Unit): HTMLElement {
        runBuild(this, null, build)
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
    //   Miscellaneous utilities
    // -------------------------------------------------------------------------

    /**
     * Docks this element. When title is not specified tries to translate
     * the class name from the string store.
     */
    fun dock(iconSource: ZkIconSource, title: String? = null) = ZkDockedElement(
        iconSource,
        title ?: localizedStrings.getNormalized(this::class.simpleName !!),
        ZkDockedElementState.Normal,
        this
    ).run()

    // -------------------------------------------------------------------------
    //   Executor permissions, logged in etc
    // -------------------------------------------------------------------------


    /**
     * Check if the executor has the given role.
     */
    fun hasRole(roleName: String) = roleName in executor.roles

    /**
     * Execute the builder function when the user **is not** the
     * anonymous user, i.e. is a logged in user.
     */
    @PublicApi
    fun ifNotAnonymous(builder: ZkElement.() -> Unit) {
        if (application.executor.anonymous) return
        this.builder()
    }

    /**
     * Execute the builder function when the user **is the
     * anonymous user, i.e. is **not** a logged in user.
     */
    @PublicApi
    fun ifAnonymous(builder: ZkElement.() -> Unit) {
        if (! application.executor.anonymous) return
        this.builder()
    }

    /**
     * Execute the builder function when the user **has**
     * the given role.
     */
    @PublicApi
    fun withRole(role: String, builder: ZkElement.() -> Unit) {
        if (role !in application.executor.roles) return
        this.builder()
    }

    /**
     * Execute the builder function when the user **has**
     * the given role.
     */
    @PublicApi
    fun withOneOfRoles(vararg roles: String, builder: ZkElement.() -> Unit) {
        roles.forEach {
            if (it in application.executor.roles) {
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
        if (role in application.executor.roles) return
        this.builder()
    }

}
