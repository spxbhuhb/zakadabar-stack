/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionDto
import zakadabar.stack.data.action.ActionDtoCompanion
import zakadabar.stack.data.schema.DtoSchema

/**
 * Password change data. Account id is present to allow administrators change the
 * password of other accounts.
 */
@Serializable
data class PasswordChangeAction(

    var accountId: Long,
    var oldPassword: Secret,
    var newPassword: Secret

) : ActionDto<ActionStatusDto> {

    companion object : ActionDtoCompanion<PasswordChangeAction>()

    override suspend fun execute() = comm().action(this, serializer(), ActionStatusDto.serializer())

    override fun schema() = DtoSchema {
        + ::accountId
        + ::oldPassword
        + ::newPassword blank false min 8
    }

}