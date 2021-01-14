/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class LoginDto(

    override var id: RecordId<LoginDto> = 0,
    var account: String = "",
    var password: String = ""

) : RecordDto<LoginDto> {

    companion object : RecordDtoCompanion<LoginDto>() {
        override val recordType = "session/login"
    }

    override fun schema() = DtoSchema.build {
        + ::account min 1 max 50 blank false
        + ::password min 1 max 50 blank false
    }

    override fun getRecordType() = recordType

    override fun comm() = LoginDto.comm
}