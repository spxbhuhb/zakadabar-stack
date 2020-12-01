/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

/**
 * A role which has some business specific meaning. May be granted to
 * principals by adding a [RoleGrantDto] child to the given principal entity.
 */
@Serializable
class RoleDto(

    override val id: Long,
    val name: String

) : RecordDto<RoleDto> {

    companion object : RecordDtoCompanion<RoleDto>() {
        override val recordType = "${Stack.shid}/role"
    }

    override fun getRecordType() = recordType

    override fun comm() = comm
}