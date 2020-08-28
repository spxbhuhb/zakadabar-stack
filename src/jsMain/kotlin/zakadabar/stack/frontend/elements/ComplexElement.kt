/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.clear
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import zakadabar.stack.frontend.FrontendContext.dispatcher
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.util.log
import zakadabar.stack.messaging.Message
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass

open class ComplexElement(
    element: HTMLElement = document.createElement("div") as HTMLElement
) : SimpleElement(element) {

    /**
     * The event that is currently processed by this element. This variable is here
     * so we don't have to pass event objects all around. The event wrapper function
     * sets/clears this method. When it is null there is no event handling in process.
     */
    var lastEvent: Event? = null

    val childElements by lazy { mutableListOf<SimpleElement>() }

    private val eventListeners by lazy { mutableListOf<Pair<String, (Event) -> Unit>>() }

    private val messageHandlers by lazy { mutableListOf<Pair<KClass<*>, (Message) -> Unit>>() }

    override fun init(): ComplexElement {
        return this
    }

    override fun cleanup(): ComplexElement {
        clearChildren()
        cleanupEventListeners()
        cleanupMessageHandlers()
        element.clear()
        return this
    }

    // ---- DOM Builder

    override infix fun build(build: DOMBuilder.() -> Unit): ComplexElement {
        val builder = DOMBuilder(element, this)
        builder.build()
        return this
    }

    // ---- Utilities --------
    // We have to redefine these to return with ComplexElement instead of SimpleElement.
    // This is a bit of hassle but adding generics to SimpleElement seems to be even worse.
    // This makes builder working properly for ComplexElement.
    // TODO check if we can modify the builder so that we get rid of these.

    override fun hide(): ComplexElement {
        classList.add(coreClasses.hidden)
        return this
    }

    override fun show(): ComplexElement {
        classList.remove(coreClasses.hidden)
        return this
    }

    override fun focus(): ComplexElement {
        element.focus()
        return this
    }

    override infix fun marginRight(size: Any): ComplexElement {
        super.marginRight(size)
        return this
    }

    override infix fun marginBottom(size: Any): ComplexElement {
        super.marginBottom(size)
        return this
    }

    override infix fun width(value: Any): ComplexElement {
        super.width(value)
        return this
    }

    override infix fun flex(value: String): ComplexElement {
        super.flex(value)
        return this
    }

    override fun mainContent(): ComplexElement {
        classList += coreClasses.mainContent
        return this
    }

    override fun withClass(className: String): ComplexElement {
        element.classList.add(className)
        return this
    }

    override infix fun cssClass(className: String): ComplexElement {
        super.cssClass(className)
        return this
    }

    override fun withClass(vararg classNames: String): ComplexElement {
        element.classList.add(*classNames)
        return this
    }

    override fun withOptionalClass(className: String): ComplexElement {
        if (element.className.isBlank()) {
            element.className = className
        }
        return this
    }

    // ---- Child elements  ----

    fun clearChildren(): ComplexElement {
        if (::childElements.isInitialized) {
            childElements.forEach { child -> child.cleanup() }
        }
        childElements.clear()
        return this
    }

    operator fun plusAssign(children: List<SimpleElement>) {
        children.forEach { child ->
            childElements += child.init()
            this.element.appendChild(child.element)
        }
    }

    operator fun plusAssign(child: SimpleElement?) {
        if (child == null) return
        childElements += child.init()
        this.element.appendChild(child.element)
    }

    @PublicApi
    infix fun insertFirst(child: SimpleElement?) {
        if (child == null) return
        childElements.add(0, child.init())
        this.element.insertBefore(child.element, this.element.firstChild)
    }

    fun insertAfter(child: SimpleElement?, after: SimpleElement?) {
        if (child == null) return
        if (after == null) {
            insertFirst(child)
        } else {
            val index = childElements.indexOf(after)
            when (index) {
                - 1 -> plusAssign(child)
                childElements.lastIndex -> plusAssign(child)
                else -> {
                    childElements.add(index + 1, child.init())
                    this.element.insertBefore(child.element, childElements[index].element.nextElementSibling)
                }
            }
        }
    }

    fun insertBefore(child: SimpleElement?, before: SimpleElement?) {
        if (child == null) return

        if (before == null) {
            plusAssign(child)
            return
        }

        val index = childElements.indexOf(before)

        if (index == - 1) {
            plusAssign(child)
        } else {
            childElements.add(index, child.init())
            this.element.insertBefore(child.element, childElements[index].element)
        }

    }

    operator fun minusAssign(child: SimpleElement?) {
        if (child == null) return

        childElements -= child

        child.cleanup()
        child.element.remove()
    }

    fun hasChildOf(clazz: KClass<*>): Boolean {
        for (child in childElements) {
            if (clazz.isInstance(child)) return true
        }
        return false
    }

    // ---- Event listeners ----

    fun on(type: String, listener: (() -> Unit)?) {
        if (listener == null) return

        // wrap the listener so we won't have crazy exception strings all around
        // this also helps with removal as the object will be the same as we added

        addWrapped(type) { event ->
            try {
                lastEvent = event
                listener()
            } catch (ex: Throwable) {
                onException(ex)
            } finally {
                lastEvent = null
            }
        }
    }

    fun on(type: String, listener: ((Event) -> Unit)?) {
        if (listener == null) return

        // wrap the listener so we won't have crazy exception strings all around
        // this also helps with removal as the object will be the same as we added

        addWrapped(type) { event ->
            try {
                lastEvent = event
                listener(event)
            } catch (ex: Throwable) {
                onException(ex)
            } finally {
                lastEvent = null
            }
        }

    }

    /**
     * Add a wrapped event listener. Keep it private, so users won't start to use it.
     */
    private fun addWrapped(type: String, wrapper: ((Event) -> Unit)) {

        eventListeners.add(type to wrapper)

        // sanity check, this means that some code adds listeners again and again

        if (eventListeners.size > 100) {
            window.alert("Internal program error (more than 100 listeners) in ${this::class.simpleName}")
        }

        // finally, we are ready to add the listener to the HTML element

        element.addEventListener(type, wrapper)

    }

    fun off(typeToRemove: String) {
        eventListeners.forEach { (type, wrapper) ->
            if (type == typeToRemove) {
                element.removeEventListener(type, wrapper)
            }
        }
    }

    private fun cleanupEventListeners() {
        if (::eventListeners.isInitialized) {
            eventListeners.forEach { (type, wrapper) -> element.removeEventListener(type, wrapper) }
        }
    }

    open fun onException(ex: Throwable) = log(ex)

    // ---- Message handlers ----

    fun <T : Message> on(type: KClass<T>, handler: (T) -> Unit) {

        // wrap the handler so we won't have crazy exception strings all around
        // this also helps with removal as the object will be the same as we added

        val wrapper = fun(message: T) {
            try {
                handler(message)
            } catch (ex: Throwable) {
                onException(ex)
            }
        }

        @Suppress("UNCHECKED_CAST")
        messageHandlers.add((type as KClass<*>) to (handler as ((Message) -> Unit)))

        // sanity check, this means that some code adds handlers again and again

        if (messageHandlers.size > 100) {
            window.alert("Internal program error (more than 100 handlers) in ${this::class.simpleName}")
        }

        // finally, we can subscribe for messages

        dispatcher.subscribe(type, wrapper)

    }

    fun <T : Message> off(typeToRemove: KClass<T>) {
        messageHandlers.forEach { (type, wrapper) ->
            if (type == typeToRemove) {
                @Suppress("UNCHECKED_CAST")
                dispatcher.unsubscribe(type as KClass<Message>, wrapper)
            }
        }
    }

    private fun cleanupMessageHandlers() {
        if (::messageHandlers.isInitialized) {
            messageHandlers.forEach { (type, wrapper) ->
                @Suppress("UNCHECKED_CAST")
                dispatcher.unsubscribe(type as KClass<Message>, wrapper)
            }
        }
    }

}
