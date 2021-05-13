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
import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema
import zakadabar.stack.util.version

@Serializable
data class ServerDescriptionDto(

    // empty record id here is OK as this is a setting DTO, there is no id
    // TODO remove id field and convert this into a simple DtoBase, it is part of SessionDto anyway
    override var id: RecordId<ServerDescriptionDto> = EmptyRecordId(),

    var name: String,
    var version: String,
    var defaultLocale : String,
    var tags: List<String> = emptyList()

) : RecordDto<ServerDescriptionDto> {

    companion object : RecordDtoCompanion<ServerDescriptionDto>("server-description")

    override fun getDtoNamespace() = ServerDescriptionDto.dtoNamespace
    override fun comm() = ServerDescriptionDto.comm

    override fun schema() = DtoSchema {
        + ::name min 1 max 100 blank false
        + ::version min 1 max 20 blank false
        + ::defaultLocale min 2 max 5 blank false
        // + ::tags - not supported yet
    }
}