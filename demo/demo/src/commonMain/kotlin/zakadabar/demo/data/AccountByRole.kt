/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.builtin.account.AccountPublicDto
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion

@Serializable
data class AccountByRole(
    val roleName: String
) : QueryDto<AccountPublicDto> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(AccountPublicDto.serializer()))

    companion object : QueryDtoCompanion<AccountPublicDto>(AccountPublicDto.recordType)

}
