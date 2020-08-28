/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
