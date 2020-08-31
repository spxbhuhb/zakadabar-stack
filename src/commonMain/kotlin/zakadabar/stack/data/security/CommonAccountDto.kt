/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.security

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.DtoWithEntityCompanion
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.data.schema.DtoSchema
import zakadabar.stack.extend.DtoWithEntityContract

@Serializable
data class CommonAccountDto(

    override var id: Long = 0,
    override var entityRecord: EntityRecordDto? = null,

    var emailAddress: String = "",
    var fullName: String = "",
    var displayName: String = "",
    var organizationName: String = "",
    var avatar: Long? = null

) : DtoWithEntityContract<CommonAccountDto> {

    companion object : DtoWithEntityCompanion<CommonAccountDto>() {
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