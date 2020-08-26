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

import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.header.Header
import zakadabar.stack.frontend.builtin.util.header.HeaderClasses.Companion.headerClasses
import zakadabar.stack.frontend.elements.ComplexElement

class NewEntityHeader(
    private val newEntity: NewEntity
) : Header(
    title = t("create")
) {

    private val repeatIcon = Icons.repeat.complex16(::onRepeat)

    override fun init(): ComplexElement {
        super.init()

        toolElement += repeatIcon.withClass(headerClasses.extensionIcon16)
        toolElement += Icons.helpOutline.simple16.withClass(headerClasses.extensionIcon16)
        toolElement += Icons.close.complex16(::onCancel).withClass(headerClasses.extensionIcon16)

        return this
    }

    private fun onCancel() {
        newEntity.close()
    }

    private fun onRepeat() {
        newEntity.repeat = ! newEntity.repeat
        if (newEntity.repeat) {
            repeatIcon.element.classList.add(headerClasses.activeExtensionIcon)
        } else {
            repeatIcon.element.classList.remove(headerClasses.activeExtensionIcon)
        }
    }

}