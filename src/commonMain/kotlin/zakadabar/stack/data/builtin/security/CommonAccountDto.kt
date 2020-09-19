/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.security

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.data.entity.EntityDtoCompanion
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class CommonAccountDto(

    override var id: Long = 0,
    override var entityRecord: EntityRecordDto? = null,

    var emailAddress: String = "",
    var fullName: String = "",
    var displayName: String = "",
    var organizationName: String = "",
    var avatar: Long? = null,
    var password: String = ""

) : EntityDto<CommonAccountDto> {

    companion object : EntityDtoCompanion<CommonAccountDto>() {
        override val type = "${Stack.shid}/account"
    }

    override fun schema() = DtoSchema.build {
        + ::emailAddress max 100
        + ::fullName min 2 max 100
        + ::displayName max 20
        + ::organizationName max 100
    }

    override fun getType() = type

    override fun comm() = comm

}