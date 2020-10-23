/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.holygrail.data.rabbit

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.samples.holygrail.HolyGrail
import zakadabar.stack.data.query.Queries
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class RabbitDto(

    override val id: Long,
    val name: String,
    val color: String

) : RecordDto<RabbitDto> {

    companion object : RecordDtoCompanion<RabbitDto>() {

        override val type = "${HolyGrail.shid}/rabbit"

        override val queries = Queries.build {
            + RabbitSearch.Companion
            + RabbitColors.Companion
        }

    }

    override fun schema() = DtoSchema.build {
        + ::name max 20 min 2 notEquals "Caerbannog"
    }

    override fun getType() = type

    override fun comm() = RabbitDto.comm
}

@Serializable
data class RabbitSearch(
    val name: String
) : QueryDto {
    suspend fun execute() = comm.query(this, serializer())

    companion object : QueryDtoCompanion(RabbitDto.Companion)
}

@Serializable
data class Color(
    val color: String
)

@Serializable
data class RabbitColors(
    val rabbitNames: List<String> = emptyList()
) : QueryDto {
    suspend fun execute() = comm.query(this, serializer(), ListSerializer(Color.serializer()))

    companion object : QueryDtoCompanion(RabbitDto.Companion)
}