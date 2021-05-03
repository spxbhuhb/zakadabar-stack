/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.demo.marina.data.ship

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.demo.marina.data.PortDto
import zakadabar.demo.marina.data.SeaDto
import zakadabar.demo.marina.data.speed.SpeedDto
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.PublicApi

@Serializable
@PublicApi
data class SearchShipsQuery(
    var name: String?,
    var speed: RecordId<SpeedDto>?,
    var sea: RecordId<SeaDto>?,
    var port: RecordId<PortDto>?,
) : QueryDto<SearchShipsResult> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(SearchShipsResult.serializer()))

    companion object : QueryDtoCompanion<SearchShipsResult>(ShipDto.dtoNamespace)

}