/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util

import zakadabar.stack.data.builtin.security.CommonAccountDto
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.simple.SimpleDateTime
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.util.PublicApi

@PublicApi
open class EntityStatusCard(val dto: EntityRecordDto) : ZkElement() {

    override fun init(): EntityStatusCard {

        launch {
            val accountDto = CommonAccountDto.comm.read(dto.modifiedBy)
            val avatar = accountDto.avatar

            this cssClass coreClasses.row build {

                if (avatar != null) {
                    + image(avatar)
                } else {
                    + Initials(accountDto.displayName)
                }

                + gap(width = 10)

                + column(coreClasses.smallInfo) {

                    htmlElement.style.alignSelf = "center"

                    + row {
                        + t("modifiedBy")
                        + accountDto.displayName
                    }
                    + row {
                        + t("modifiedAt")
                        + SimpleDateTime(dto.modifiedAt)
                    }
                }
            }
        }

        return this
    }
}