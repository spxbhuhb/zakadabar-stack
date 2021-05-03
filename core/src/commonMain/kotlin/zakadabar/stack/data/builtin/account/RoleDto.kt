/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

/**
 * A role which has some business specific meaning.
 */
@Serializable
class RoleDto(

    override var id: RecordId<RoleDto>,
    var name: String,
    var description: String

) : RecordDto<RoleDto> {

    companion object : RecordDtoCompanion<RoleDto>("role")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name min 1 max 50 blank false
        + ::description
    }
}