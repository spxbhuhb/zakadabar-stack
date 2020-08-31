/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.DtoWithEntityCompanion
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.extend.DtoWithEntityContract
import zakadabar.stack.util.PublicApi

@PublicApi
@Serializable
data class SystemDto(

    override val id: Long,
    override val entityRecord: EntityRecordDto?

) : DtoWithEntityContract<SystemDto> {

    override fun getType() = type

    override fun comm() = comm

    companion object : DtoWithEntityCompanion<SystemDto>() {
        override val type = "${Stack.shid}/system"
    }

}