/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionDto
import zakadabar.stack.data.action.ActionDtoCompanion
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class LoginAction(

    var accountName: String,
    var password: Secret

) : ActionDto<ActionStatusDto> {

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusDto.serializer())

    companion object : ActionDtoCompanion<ActionStatusDto>(SessionDto.recordType)

    override fun schema() = DtoSchema {
        + ::accountName min 1 max 50 blank false
        + ::password min 1 max 50 blank false
    }

}