/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class BlobDto(

    override val id: RecordId<BlobDto>,
    var name: String,
    var type: String,
    var size: Long

) : RecordDto<BlobDto> {

    companion object : RecordDtoCompanion<BlobDto>() {
        override val recordType = "${Stack.shid}/blob"
    }

    override fun schema() = DtoSchema.build {
        + ::name min 1 max 200
        + ::type min 1 max 100
        + ::size min 0 max Long.MAX_VALUE
    }

    override fun getRecordType() = recordType

    override fun comm() = BlobDto.comm
}