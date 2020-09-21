/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion

@Serializable
class ChildrenQuery(
    val parentId: Long?
) : QueryDto {

    suspend fun execute() = comm.query(this, serializer(), ListSerializer(EntityRecordDto.serializer()))

    companion object : QueryDtoCompanion(EntityRecordDto.Companion)

}

