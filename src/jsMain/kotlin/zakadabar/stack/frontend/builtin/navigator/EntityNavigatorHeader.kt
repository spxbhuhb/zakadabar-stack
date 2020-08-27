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
import zakadabar.stack.frontend.comm.rest.EntityCache
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.util.launch

class EntityNavigatorHeader(
    private val navigator: EntityNavigator
) : Header(
    icon = Icons.globe.simple18.withClass(headerClasses.headerIcon18),
    title = t("navigation")
) {

    override fun init(): ComplexElement {
        super.init()

        toolElement += Icons.filterAlt.simple16.withClass(headerClasses.extensionIcon16)
        toolElement += Icons.addBox.complex16(::onNewEntity).withClass(headerClasses.extensionIcon16)
        toolElement += Icons.helpOutline.simple16.withClass(headerClasses.extensionIcon16)

        return this
    }

    private fun onNewEntity() {
        // to prevent more new entity elements at the same time
        if (navigator.hasChildOf(NewEntity::class)) return

        launch {
            val parentId = navigator.currentEntityId
            val newEntity = NewEntity(navigator, if (parentId == null) null else EntityCache.get(parentId))
            navigator.insertAfter(newEntity, navigator.header)
            newEntity.focus()
        }
    }

}