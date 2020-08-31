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

package zakadabar.stack.frontend.builtin.util.image

import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.extend.ScopedViewContract
import zakadabar.stack.util.PublicApi

@PublicApi
class ImageView(private val entityDto: EntityDto) : ComplexElement() {

    companion object : ScopedViewContract() {

        override fun newInstance(scope: Any?) = ImageView(scope as EntityDto)

    }

    override fun init(): ImageView {

        val url = EntityDto.revisionUrl(entityDto.id)

        element.innerHTML = """<img src="$url" class="${coreClasses.whMax100p}" >"""

        return this
    }

}