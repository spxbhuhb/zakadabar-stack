/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class SeaDto(

    override var id: RecordId<SeaDto>,
    var name: String,

    ) : RecordDto<SeaDto> {

    companion object : RecordDtoCompanion<SeaDto>({
        namespace = "sea"
    })

    override fun getRecordType() = namespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name min 1 max 30
    }

}