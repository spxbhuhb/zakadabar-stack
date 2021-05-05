/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class ExampleReferenceDto(

    override var id: RecordId<zakadabar.lib.examples.data.builtin.ExampleReferenceDto>,
    var name: String

) : RecordDto<zakadabar.lib.examples.data.builtin.ExampleReferenceDto> {

    companion object : RecordDtoCompanion<zakadabar.lib.examples.data.builtin.ExampleReferenceDto>("example-reference")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name max 50
    }

}