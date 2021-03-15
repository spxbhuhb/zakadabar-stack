/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data.ship

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.demo.data.speed.SpeedDto
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion

@Serializable
data class ShipSpeeds(
    val names: List<String> = emptyList()
) : QueryDto<SpeedDto> {
    override suspend fun execute() = comm().query(this, serializer(), ListSerializer(SpeedDto.serializer()))

    companion object : QueryDtoCompanion<ShipDto, ShipDto>()
}