/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.data.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

/**
 * A role which has some business specific meaning.
 */
@Serializable
class RoleDto(

    override var id: Long,
    var name: String,
    var description: String = ""

) : RecordDto<RoleDto> {

    companion object : RecordDtoCompanion<RoleDto>() {
        override val recordType = "role"
    }

    override fun getRecordType() = recordType

    override fun comm() = comm
}