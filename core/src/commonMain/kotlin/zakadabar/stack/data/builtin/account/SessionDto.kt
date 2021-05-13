/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import zakadabar.stack.data.builtin.misc.ServerDescriptionDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId

@Serializable
data class SessionDto(

    override var id: RecordId<SessionDto>,
    val account: AccountPublicDto,
    val anonymous: Boolean,
    val roles: List<String>,
    val serverDescription: ServerDescriptionDto

) : RecordDto<SessionDto> {

    companion object : RecordDtoCompanion<SessionDto>("session")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

}