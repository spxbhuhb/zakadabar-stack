/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(
    InstantAsStringSerializer::class,
    OptInstantAsStringSerializer::class
)

package zakadabar.stack.data.schema.descriptor

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.util.InstantAsStringSerializer
import zakadabar.stack.data.util.OptInstantAsStringSerializer
import zakadabar.stack.util.UUID

@Serializable
sealed class PropertyDto : DtoBase {
    abstract val name: String
    abstract val validations: List<ValidationDto>
}

@Serializable
class RecordIdPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    val kClassName: String,
    var defaultValue: RecordId<DtoBase>,
    var value: RecordId<DtoBase>
) : PropertyDto()

@Serializable
class BooleanPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Boolean,
    var value: Boolean
) : PropertyDto()

@Serializable
class DoublePropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Double,
    var value: Double
) : PropertyDto()

@Serializable
class DtoBasePropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var descriptor: DescriptorDto
) : PropertyDto()

@Serializable
class EnumPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var enumValues: List<String>,
    var defaultValue: String,
    var value: String
) : PropertyDto()

@Serializable
class InstantPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    @Serializable(OptInstantAsStringSerializer::class)
    var defaultValue: Instant?,
    @Serializable(InstantAsStringSerializer::class)
    var value: Instant
) : PropertyDto()

@Serializable
class IntPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Int,
    var value: Int
) : PropertyDto()

@Serializable
class LongPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Long,
    var value: Long
) : PropertyDto()

@Serializable
class SecretPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Secret,
    var value: Secret
) : PropertyDto()

@Serializable
class StringPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: String,
    var value: String
) : PropertyDto()

@Serializable
class UuidPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: UUID,
    var value: UUID
) : PropertyDto()

@Serializable
class OptRecordIdPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var kClass: String,
    var defaultValue: RecordId<DtoBase>?,
    var value: RecordId<DtoBase>?
) : PropertyDto()

@Serializable
class OptBooleanPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Boolean?,
    var value: Boolean?
) : PropertyDto()

@Serializable
class OptDoublePropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Double?,
    var value: Double?
) : PropertyDto()

@Serializable
class OptEnumPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var enumValues: List<String>,
    var defaultValue: String?,
    var value: String?
) : PropertyDto()

@Serializable
class OptInstantPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Instant?,
    var value: Instant?
) : PropertyDto()

@Serializable
class OptIntPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Int?,
    var value: Int?
) : PropertyDto()

@Serializable
class OptLongPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Long?,
    var value: Long?
) : PropertyDto()

@Serializable
class OptSecretPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: Secret?,
    var value: Secret?
) : PropertyDto()

@Serializable
class OptStringPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: String?,
    var value: String?
) : PropertyDto()

@Serializable
class OptUuidPropertyDto(
    override val name: String,
    override var validations: List<ValidationDto>,
    var defaultValue: UUID?,
    var value: UUID?
) : PropertyDto()