/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util

import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.extend.ViewCompanion
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass

/**
 * A view that switches between other views.
 *
 * @sample [zakadabar.stack.frontend.builtin.input.EditableText]
 */
open class SwitchView : ZkElement() {

    protected val views = mutableListOf<() -> SwitchableElement>()

    private var index = 0

    private var removed = false // to prevent new view creation by callbacks after the element is removed

    @PublicApi
    protected var activeView: ZkElement? = null

    override fun init(): ZkElement {
        switch(index)
        return this
    }

    override fun cleanup(): ZkElement {
        removed = true
        return super.cleanup()
    }

    /**
     * Switch to an specific view. Sets the index to [to].
     *
     * @param  to  The index of the view in [views] to switch to.
     */
    @PublicApi
    open fun switch(to: Int) {
        if (removed) return

        val view = views[to]()

        view.switchView = this

        index = to

        this -= activeView
        activeView = view
        this += view
    }

    /**
     * Switch to the next view.
     *
     * @param  roll    When true the last view will be switched to the first one.
     * @param  force   When true the last view will be reloaded.
     */
    @PublicApi
    fun next(roll: Boolean = false, force: Boolean = false) {
        switch(
            when {
                index < views.lastIndex -> index + 1
                roll -> 0
                force -> index
                else -> return
            }
        )
    }

    /**
     * Switch to the previous view.
     *
     * @param  roll    When true the first view will be switched to the last one.
     * @param  force   When true the first view will be reloaded.
     */
    @PublicApi
    fun previous(roll: Boolean = false, force: Boolean = false) {
        switch(
            when {
                index > 0 -> index - 1
                roll -> views.lastIndex
                force -> index
                else -> return
            }
        )
    }

    /**
     * Switch to a view with the specified contract.
     *
     * @param  viewClass  The view contract to switch to.
     */
    @PublicApi
    fun to(viewClass: KClass<ViewCompanion>) {
        val to = views.indexOfFirst { viewClass.isInstance(it) }
        require(to > 0) { "cannot find view to switch to: ${viewClass.simpleName}" }
        switch(to)
    }
}