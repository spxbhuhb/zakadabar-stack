/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data.ship

import kotlinx.serialization.*
import zakadabar.demo.data.AccountPrivateDto
import zakadabar.demo.data.PortDto
import zakadabar.demo.data.speed.SpeedDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class ShipDto(

    override var id: RecordId<ShipDto>,
    var name: String,
    var speed: RecordId<SpeedDto>,
    var captain: RecordId<AccountPrivateDto>,
    var description: String,
    var hasPirateFlag: Boolean,
    var port: RecordId<PortDto>?

) : RecordDto<ShipDto> {

    companion object : RecordDtoCompanion<ShipDto>({
        recordType = "ship"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name max 20 min 2 notEquals "Titanic" default "Titanic"
        + ::speed min 1
        + ::captain min 1
        + ::description max 2000
        + ::hasPirateFlag default false
        + ::port
    }

}