/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data.speed

import kotlinx.serialization.*
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class SpeedDto(

    override var id: RecordId<SpeedDto>,
    var description: String,
    var value: SpeedValues

) : RecordDto<SpeedDto> {

    companion object : RecordDtoCompanion<SpeedDto>({
        namespace = "speed"
    })

    override fun getRecordType() = namespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::description min 1 max 100
        + ::value
    }

}