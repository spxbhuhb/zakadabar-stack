/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionDto
import zakadabar.stack.data.action.ActionDtoCompanion
import zakadabar.stack.data.builtin.ActionStatusDto

@Serializable
class LogoutAction : ActionDto<ActionStatusDto> {

    override suspend fun execute() = comm().action(this, serializer(), ActionStatusDto.serializer())

    companion object : ActionDtoCompanion<ActionStatusDto>()

}