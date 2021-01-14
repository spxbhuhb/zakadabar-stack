/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

@Serializable
data class SessionDto(

    override var id: Long,
    val accountId: Long,
    val displayName: String,
    val roles: List<String>

) : RecordDto<SessionDto> {

    companion object : RecordDtoCompanion<SessionDto>() {
        override val recordType = "session"
    }

    override fun getRecordType() = recordType

    override fun comm() = SessionDto.comm

}