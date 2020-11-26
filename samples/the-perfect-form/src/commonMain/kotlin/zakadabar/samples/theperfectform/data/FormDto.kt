/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theperfectform.data

import kotlinx.serialization.Serializable
import zakadabar.samples.theperfectform.PerfectForm
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.schema.DtoSchema

@Serializable
class FormDto(

    override val id: Long,
    val textField : String = ""

) : RecordDto<FormDto> {

    companion object : RecordDtoCompanion<FormDto>() {

        override val type = "${PerfectForm.shid}/form"

    }

    override fun schema() = DtoSchema.build {
        + ::textField min 2 max 4 notEquals "abcd"
    }

    override fun getType() = recordType

    override fun comm() = FormDto.comm

}