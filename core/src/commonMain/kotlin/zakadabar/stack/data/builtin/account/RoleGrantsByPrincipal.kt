/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion

@Serializable
data class RoleGrantsByPrincipal(
    val principal: Long
) : QueryDto<RoleGrantDto> {

    override suspend fun execute() = comm().query(this, serializer(), ListSerializer(RoleGrantDto.serializer()))

    companion object : QueryDtoCompanion<RoleGrantDto>()

}