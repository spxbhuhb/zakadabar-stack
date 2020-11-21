/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data

import kotlinx.serialization.Serializable
import zakadabar.demo.Demo
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class SpeedDto(

    override val id: RecordId<SpeedDto>,
    var description: String,
    var value: Double

) : RecordDto<SpeedDto> {

    companion object : RecordDtoCompanion<SpeedDto>() {
        override val type = "${Demo.shid}/speed"
    }

    override fun schema() = DtoSchema.build {
        + ::description min 1 max 100
    }

    override fun getType() = type

    override fun comm() = SpeedDto.comm
}