/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion

@Serializable
data class ShipSearch(
    val name: String
) : QueryDto<ShipDto> {
    override suspend fun execute() = comm.query(this, serializer())

    companion object : QueryDtoCompanion<ShipDto, ShipDto>(ShipDto)
}
