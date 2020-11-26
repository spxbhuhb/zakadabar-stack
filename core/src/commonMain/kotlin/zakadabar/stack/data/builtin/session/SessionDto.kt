/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package zakadabar.stack.data.builtin.session

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

@Serializable
data class SessionDto(

    override val id: Long

) : RecordDto<SessionDto> {

    companion object : RecordDtoCompanion<SessionDto>() {
        override val recordType = "${Stack.shid}/session"
    }

    override fun getRecordType() = recordType

    override fun comm() = SessionDto.comm

}