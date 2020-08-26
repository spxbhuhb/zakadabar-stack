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
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.input.Input
import zakadabar.stack.frontend.builtin.navigator.NavigatorClasses.Companion.navigatorClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.util.launch

abstract class NewEntityItemWithName(
    private val newEntity: NewEntity
) : ComplexElement() {

    private var changed = false
    private val input = Input(::onEnter, newEntity::close, ::onChange, placeholder = t("typeName"))
    private val nextIcon = Icons.arrowRight.complex18(::onNext)

    override fun init(): NewEntityItemWithName {
        super.init()

        className = coreClasses.w100Row

        this += input.withClass(coreClasses.grow)
        this += nextIcon.withClass(navigatorClasses.newEntityActionIcon).hide()

        return this
    }

    override fun focus(): NewEntityItemWithName {
        input.focus()
        return this
    }

    private fun onChange(s: String) {
        changed = true
        if (s.isNotBlank()) nextIcon.show() else nextIcon.hide()
    }

    private fun onNext() = next(input.value)

    private fun onEnter(s: String) = next(s)

    fun next(s: String) {

        if (s.isBlank() || ! changed) return

        launch {
            create(newEntity.parentDto, s)
        }

        if (! newEntity.repeat) {
            newEntity.close()
        } else {
            changed = false
        }
    }

    abstract suspend fun create(parentDto: EntityDto?, name: String)
}
