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
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.navigator.NavigatorClasses.Companion.navigatorClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.util.svg

class EntityNavigatorItem(
    private val idPrefix: String,
    private val entityDto: EntityDto
) : ComplexElement() {

    override fun init(): EntityNavigatorItem {
        val support = FrontendContext.entitySupports[entityDto.type]

        val iconSource = support?.iconSource ?: Icons.description
        val typeName = support?.displayName ?: FrontendContext.t("entity")

        var html = ""

        html += """<div id="${idPrefix}${entityDto.id}" class="${navigatorClasses.entityListItem}">"""
        html += svg(iconSource.content, navigatorClasses.entityListIcon, 16) // FIXME cache icons somehow
        html += """<div class=${navigatorClasses.entityListName}>${entityDto.name}</div>"""
        html += """<div class=${navigatorClasses.entityListDetail}>${typeName} #${entityDto.id}</div>"""
        html += "</div>"

        innerHTML = html

        return this
    }

}
