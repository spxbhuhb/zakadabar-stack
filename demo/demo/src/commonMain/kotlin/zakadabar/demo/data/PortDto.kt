/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data

import kotlinx.serialization.*
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class PortDto(

    override var id: RecordId<PortDto>,
    var name: String,
    var sea: RecordId<SeaDto>

) : RecordDto<PortDto> {

    companion object : RecordDtoCompanion<PortDto>({
        namespace = "port"
    })

    override fun getRecordType() = namespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name max 30 min 2
        + ::sea
    }

}