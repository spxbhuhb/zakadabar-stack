/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.demo.data.ship

import kotlinx.serialization.builtins.ListSerializer
import zakadabar.demo.data.PortDto
import zakadabar.demo.data.SeaDto
import zakadabar.demo.data.speed.SpeedDto
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

    // Do not forget to add query and action classes to ShipDto!

    override suspend fun execute() = comm().query(this, serializer(), ListSerializer(SearchShipsResult.serializer()))

    companion object : QueryDtoCompanion<SearchShipsResult>()

}