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
package zakadabar.stack.frontend.builtin.util

import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.SwitchableElement
import zakadabar.stack.frontend.extend.ViewContract
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass

/**
 * A view that switches between other views.
 *
 * @sample [zakadabar.stack.frontend.builtin.input.EditableText]
 */
open class SwitchView : ComplexElement() {

    protected val views = mutableListOf<() -> SwitchableElement>()

    private var index = 0

    private var removed = false // to prevent new view creation by callbacks after the element is removed

    @PublicApi
    protected var activeView: ComplexElement? = null

    override fun init(): ComplexElement {
        switch(index)
        return this
    }

    override fun cleanup(): ComplexElement {
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
    fun to(viewClass: KClass<ViewContract>) {
        val to = views.indexOfFirst { viewClass.isInstance(it) }
        require(to > 0) { "cannot find view to switch to: ${viewClass.simpleName}" }
        switch(to)
    }
}