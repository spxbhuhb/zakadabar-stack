/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.data

import kotlinx.serialization.Serializable
import zakadabar.samples.theplace.ThePlace
import zakadabar.stack.data.query.Queries
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class ShipDto(

    override val id: RecordId<ShipDto>,
    var name: String,
    var speed: RecordId<SpeedDto>

) : RecordDto<ShipDto> {

    companion object : RecordDtoCompanion<ShipDto>() {

        override val type = "${ThePlace.shid}/ship"

        override val queries = Queries.build {
            + ShipSearch.Companion
            + ShipSpeeds.Companion
        }

    }

    override fun schema() = DtoSchema.build {
        + ::name max 20 min 2 notEquals "Titanic"
    }

    override fun getType() = recordType

    override fun comm() = ShipDto.comm
}