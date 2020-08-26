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

package zakadabar.stack.frontend.builtin.util.header

import zakadabar.stack.frontend.builtin.simple.SimpleText
import zakadabar.stack.frontend.builtin.util.header.HeaderClasses.Companion.headerClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.SimpleElement
import zakadabar.stack.util.PublicApi

@PublicApi
open class Header(
    title: String = "",
    val icon: SimpleElement? = null,
    @PublicApi val titleElement: SimpleElement = SimpleText(title),
    private val tools: List<SimpleElement> = emptyList()
) : ComplexElement() {

    val toolElement = ComplexElement()

    override fun init(): ComplexElement {
        super.init()

        element.classList.add(headerClasses.header)

        this += icon?.withOptionalClass(headerClasses.headerIcon)
        this += titleElement.withClass(headerClasses.text)
        this += toolElement.withClass(headerClasses.extensions)

        toolElement += tools

        return this
    }

}