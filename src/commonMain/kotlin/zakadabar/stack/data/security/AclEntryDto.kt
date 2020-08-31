/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.security

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.DtoWithEntityCompanion
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.extend.DtoWithEntityContract

/**
 * An access control list entry. Used for fine grained security checks.
 *
 * @property  subjectId  The id of the entity this entry grants/denies access for.
 * @property  access     A free-form access type name. There are some common names but
 *                       anything is acceptable as long as the backend recognizes it.
 * @property  allow      When true, the access type is allowed, when false it is
 *                       explicitly denied. False stops the access check process and
 *                       denies that type of access immediately.
 */
@Serializable
data class AclEntryDto(

    override val id: Long,
    override val entityRecord: EntityRecordDto?,

    val subjectId: Long,
    val access: String,
    val allow: Boolean

) : DtoWithEntityContract<AclEntryDto> {

    override fun getType() = type

    override fun comm() = comm

    companion object : DtoWithEntityCompanion<AclEntryDto>() {
        override val type = "${Stack.shid}/acl-entry"
    }
}