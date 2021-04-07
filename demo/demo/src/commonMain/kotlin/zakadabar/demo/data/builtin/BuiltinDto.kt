/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:UseSerializers(
    InstantAsStringSerializer::class,
    OptInstantAsStringSerializer::class
)

package zakadabar.demo.data.builtin

import kotlinx.datetime.Instant
import kotlinx.serialization.*
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema
import zakadabar.stack.data.util.InstantAsStringSerializer
import zakadabar.stack.data.util.OptInstantAsStringSerializer

@Serializable
data class BuiltinDto(

    override var id: RecordId<BuiltinDto>,
    var booleanValue: Boolean,
    var doubleValue: Double,
    var enumSelectValue: ExampleEnum,
    var intValue: Int,
    @Serializable(InstantAsStringSerializer::class)
    var instantValue: Instant,
    var optBooleanValue: Boolean?,
    var optDoubleValue: Double?,
    var optEnumSelectValue: ExampleEnum?,
    @Serializable(OptInstantAsStringSerializer::class) // TODO remove when fixed:
    var optInstantValue: Instant?,
    var optIntValue: Int?,
    var optSecretValue: Secret?,
    var optRecordSelectValue: RecordId<BuiltinDto>?,
    var optStringValue: String?,
    var optStringSelectValue: String?,
    var optTextAreaValue: String?,
    var secretValue: Secret,
    var recordSelectValue: RecordId<BuiltinDto>,
    var stringValue: String,
    var stringSelectValue: String,
    var textAreaValue: String

) : RecordDto<BuiltinDto> {

    companion object : RecordDtoCompanion<BuiltinDto>({
        recordType = "builtin"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::booleanValue
        + ::doubleValue
        + ::enumSelectValue
        + ::intValue
        + ::instantValue
        + ::optBooleanValue
        + ::optDoubleValue
        + ::optEnumSelectValue
        + ::optInstantValue
        + ::optIntValue
        + ::optSecretValue
        + ::optRecordSelectValue
        + ::optStringValue
        + ::optStringSelectValue
        + ::optTextAreaValue
        + ::secretValue blank false
        + ::recordSelectValue min 1
        + ::stringValue blank false
        + ::stringSelectValue blank false
        + ::textAreaValue blank false
    }

}