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

    companion object : ActionDtoCompanion<PasswordChangeAction>(PrincipalDto.namespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusDto.serializer())

    override fun schema() = DtoSchema {
        + ::accountId
        + ::oldPassword blank false
        + ::newPassword blank false min 8
    }

}