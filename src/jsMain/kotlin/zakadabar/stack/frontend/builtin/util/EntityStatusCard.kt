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

import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.data.security.CommonAccountDto
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.simple.SimpleDateTime
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.util.PublicApi

@PublicApi
open class EntityStatusCard(val entityDto: EntityDto) : ComplexElement() {

    override fun init(): EntityStatusCard {

        launch {
            val accountDto = CommonAccountDto.comm.get(entityDto.modifiedBy)
            val avatar = accountDto.avatar

            this cssClass coreClasses.row build {

                if (avatar != null) {
                    + image(avatar)
                } else {
                    + Initials(accountDto.displayName)
                }

                + gap(width = 10)

                + column() cssClass coreClasses.smallInfo build {

                    + row {
                        + t("modifiedBy")
                        + accountDto.displayName
                    }
                    + div {
                        + t("modifiedAt")
                        + SimpleDateTime(entityDto.modifiedAt)
                    }
                }
            }
        }

        return this
    }
}