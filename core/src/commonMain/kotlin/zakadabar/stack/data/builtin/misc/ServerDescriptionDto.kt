/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.stack.data.builtin.misc

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId

@Serializable
data class ServerDescriptionDto(
    override var id: RecordId<ServerDescriptionDto>,

    var name: String,
    var version: String,
    var tags: List<String>,
    var stringsRevision: Int

) : RecordDto<ServerDescriptionDto> {

    companion object : RecordDtoCompanion<ServerDescriptionDto>("server-description")

    override fun getDtoNamespace() = ServerDescriptionDto.dtoNamespace
    override fun comm() = ServerDescriptionDto.comm

}