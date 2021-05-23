/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.lib.examples.data.builtin

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.data.schema.BoSchema

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
) : QueryBo<ExampleResult> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(ExampleResult.serializer()))

    companion object : QueryBoCompanion<BuiltinDto>(BuiltinDto.boNamespace)

    override fun schema() = BoSchema {
        + ::booleanValue
        + ::enumSelectValue
        + ::intValue
        + ::stringValue
        + ::limit min 10 max 100000 default 1000
    }

}