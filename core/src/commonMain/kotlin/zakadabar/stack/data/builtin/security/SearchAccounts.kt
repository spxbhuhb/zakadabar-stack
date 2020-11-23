/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.security

import kotlinx.serialization.Serializable
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion

/**
 * Get common account ids. Used to get the display name and avatar that belongs
 * to specific account ids.
 */
@Serializable
class SearchAccounts(
    val accountIds: List<Long>
) : QueryDto<CommonAccountDto> {

    override suspend fun execute() = comm.query(this, serializer())

    companion object : QueryDtoCompanion<CommonAccountDto,CommonAccountDto>(CommonAccountDto.Companion)

}