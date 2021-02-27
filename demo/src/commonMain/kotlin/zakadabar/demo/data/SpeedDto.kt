/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class SpeedDto(

    override var id: RecordId<SpeedDto>,
    var description: String,
    var value: Double

) : RecordDto<SpeedDto> {

    companion object : RecordDtoCompanion<SpeedDto>({
        recordType = "speed"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::description min 1 max 100
        + ::value notEquals Double.NaN
    }

}