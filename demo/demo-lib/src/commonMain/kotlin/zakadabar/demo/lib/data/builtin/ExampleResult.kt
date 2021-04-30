/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
@file:UseSerializers(
    OptInstantAsStringSerializer::class,
    InstantAsStringSerializer::class
)

package zakadabar.demo.lib.data.builtin

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.util.InstantAsStringSerializer
import zakadabar.stack.data.util.OptInstantAsStringSerializer

@Serializable
data class ExampleResult(
    val recordId: RecordId<BuiltinDto>,
    var booleanValue: Boolean,
    var enumSelectValue: ExampleEnum,
    var intValue: Int,
    var stringValue: String
) : DtoBase