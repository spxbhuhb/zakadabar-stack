/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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

                    current.style.alignSelf = "center"

                    + row {
                        + t("modifiedBy")
                        + accountDto.displayName
                    }
                    + row {
                        + t("modifiedAt")
                        + SimpleDateTime(entityDto.modifiedAt)
                    }
                }
            }
        }

        return this
    }
}