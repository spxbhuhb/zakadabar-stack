/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.examples.data.builtin

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import zakadabar.core.data.Secret
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.UUID

@Serializable
data class BuiltinBo(

    override var id: EntityId<BuiltinBo>,
    var booleanValue: Boolean,
    var doubleValue: Double,
    var enumSelectValue: ExampleEnum,
    var intValue: Int,
    var instantValue: Instant,
    var localDateValue: LocalDate,
    var localDateTimeValue: LocalDateTime,
    var optBooleanValue: Boolean?,
    var optDoubleValue: Double?,
    var optEnumSelectValue: ExampleEnum?,
    var optInstantValue: Instant?,
    var optIntValue: Int?,
    var optLocalDateValue: LocalDate?,
    var optLocalDateTimeValue: LocalDateTime?,
    var optSecretValue: Secret?,
    var optRecordSelectValue: EntityId<ExampleReferenceBo>?,
    var optStringValue: String?,
    var optStringSelectValue: String?,
    var optTextAreaValue: String?,
    var optUuidValue: UUID?,
    var secretValue: Secret,
    var recordSelectValue: EntityId<ExampleReferenceBo>,
    var stringValue: String,
    var stringSelectValue: String,
    var textAreaValue: String,
    var uuidValue: UUID

) : EntityBo<BuiltinBo> {

    companion object : EntityBoCompanion<BuiltinBo>("builtin")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    @Suppress("DuplicatedCode")
    override fun schema() = BoSchema {
        + ::id
        + ::booleanValue
        + ::doubleValue
        + ::enumSelectValue
        + ::intValue
        + ::instantValue
        + ::localDateValue
        + ::localDateTimeValue
        + ::optBooleanValue
        + ::optDoubleValue
        + ::optEnumSelectValue
        + ::optInstantValue
        + ::optLocalDateValue
        + ::optLocalDateTimeValue
        + ::optIntValue
        + ::optSecretValue max 50
        + ::optRecordSelectValue
        + ::optStringValue max 100
        + ::optStringSelectValue max 100
        + ::optTextAreaValue max 100000
        + ::optUuidValue
        + ::secretValue blank false max 50
        + ::recordSelectValue
        + ::stringValue blank false max 50
        + ::stringSelectValue blank false max 50
        + ::textAreaValue blank false max 2000
        + ::uuidValue
    }

}