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
package zakadabar.stack.frontend.builtin.navigator

import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.input.Input
import zakadabar.stack.frontend.builtin.navigator.NavigatorClasses.Companion.navigatorClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.extend.FrontendEntitySupport

/**
 * Entity creation from the global navigator. Displays list of entity types that
 * may be created as child of the given parent.
 *
 * Pay attention to root when the parent is null.
 */
class NewEntity(
    private val navigator: EntityNavigator,
    val parentDto: EntityDto?
) : ComplexElement() {

    private val header = NewEntityHeader(this)

    private val inputAndActions = ComplexElement()
    private val nextIcon = Icons.arrowRight.complex18(::next)
    private val input = Input(::onEnter, ::close, ::onChange, placeholder = t("typeToSelect"))

    private val typeList = ComplexElement()

    internal var selected: FrontendEntitySupport<*>? = null

    var repeat = false

    override fun init(): ComplexElement {
        super.init()

        className = navigatorClasses.newEntity

        this += header

        this += inputAndActions.withClass(coreClasses.w100Row)

        inputAndActions += input.withClass(coreClasses.grow)
        inputAndActions += nextIcon.withClass(navigatorClasses.newEntityActionIcon)

        nextIcon.hide()

        this += typeList.withClass(coreClasses.w100) // TODO some scrolling and height limit would be nice

        FrontendContext.entitySupports.forEach {
            if (it.value.newView != null) {
                typeList += NewEntityItem(this, it.value)
            }
        }

        input.value = ""

        return this
    }

    override fun focus() = input.focus()

    fun next() {
        if (selected == null) return

        this -= inputAndActions
        this -= typeList

        val entityBuilder = selected?.newView?.newElement(this)
        this += entityBuilder
        entityBuilder?.focus()

    }

    fun close() {
        navigator -= this
    }

    private fun onEnter(@Suppress("UNUSED_PARAMETER") s: String) = next()

    private fun onChange(s: String) {
        val trimmed = s.trim().toLowerCase()
        var count = 0

        var lastShown: FrontendEntitySupport<*>? = null

        typeList.childElements.forEach {
            if (it is NewEntityItem) {
                if (trimmed.isEmpty() || it.support.displayName.toLowerCase().contains(trimmed)) {
                    it.show()
                    count ++
                    lastShown = it.support
                } else {
                    it.hide()
                }
            }
        }

        selected = if (trimmed.isNotEmpty() && count == 1) {
            nextIcon.show()
            lastShown
        } else {
            nextIcon.hide()
            null
        }
    }

}