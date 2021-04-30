/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.demo.lib.data.builtin

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion
import zakadabar.stack.data.schema.DtoSchema

/**
 * Query class that defines the filters the query supports.
 *
 * Note that the values are all optional here. Specifying a null value for a
 * mandatory field means that the user does not want to filter on that field.
 */
@Serializable
data class ExampleQuery(
    var booleanValue: Boolean?,
    var enumSelectValue: ExampleEnum?,
    var intValue: Int?,
    var stringValue: String?,
    var limit: Int
) : QueryDto<ExampleResult> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(ExampleResult.serializer()))

    companion object : QueryDtoCompanion<BuiltinDto>(BuiltinDto.namespace)

    override fun schema() = DtoSchema {
        + ::booleanValue
        + ::enumSelectValue
        + ::intValue
        + ::stringValue
        + ::limit min 10 max 100000 default 1000
    }

}