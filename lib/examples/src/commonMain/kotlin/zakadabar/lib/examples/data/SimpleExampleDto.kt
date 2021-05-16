/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.examples.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class SimpleExampleDto(

    override var id: RecordId<SimpleExampleDto>,
    var name: String

) : RecordDto<SimpleExampleDto> {

    companion object : RecordDtoCompanion<SimpleExampleDto>(dtoNamespace = "simple-example")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name min 1 max 30 blank false default "Example Name"
    }

}