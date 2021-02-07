/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordCommInterface
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class BlobDto(

    override var id: RecordId<BlobDto>,
    var dataRecord: RecordId<*>?,
    var dataType: String,
    var name: String,
    var type: String,
    var size: Long

) : RecordDto<BlobDto> {

    companion object : RecordDtoCompanion<BlobDto>({
        recordType = "blob"
    })

    override fun schema() = DtoSchema {
        + ::name min 1 max 200
        + ::type min 1 max 100
        + ::size min 0 max Long.MAX_VALUE
    }

    override fun getRecordType() = dataType

    override fun comm(): RecordCommInterface<BlobDto> {
        throw IllegalStateException("comm of BlobDto should not be used directly")
    }

    /**
     * Get an URL for the a BLOB.
     */
    fun url() = "/api/$dataType/$dataRecord/blob/$id"

}