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
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.util.InstantAsStringSerializer
import zakadabar.stack.data.util.OptInstantAsStringSerializer
import zakadabar.stack.util.UUID

/**
 * Base class for property BOs. Decided to go with putting [optional] into
 * this class and allowing null values in descendants. This halves the number
 * of classes at the cost of some manual checks.
 */
@Serializable
sealed class BoProperty : BaseBo {
    abstract val name: String
    abstract val optional: Boolean
    abstract val constraints: List<BoConstraint>
}

@Serializable
class RecordIdBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    val kClassName: String,
    var defaultValue: RecordId<DtoBase>?,
    var value: RecordId<DtoBase>?
) : BoProperty()

@Serializable
class BooleanBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: Boolean?,
    var value: Boolean?
) : BoProperty()

@Serializable
class DoubleBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: Double?,
    var value: Double?
) : BoProperty()

@Serializable
class DtoBaseBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var value: BoDescriptor?
) : BoProperty()

@Serializable
class EnumBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var enumName: String,
    var enumValues: List<String>,
    var defaultValue: String?,
    var value: String?
) : BoProperty()

@Serializable
class InstantBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    @Serializable(OptInstantAsStringSerializer::class)
    var defaultValue: Instant?,
    @Serializable(OptInstantAsStringSerializer::class)
    var value: Instant?
) : BoProperty()

@Serializable
class IntBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: Int?,
    var value: Int?
) : BoProperty()

@Serializable
class LongBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: Long?,
    var value: Long?
) : BoProperty()

@Serializable
class SecretBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: Secret?,
    var value: Secret?
) : BoProperty()

@Serializable
class StringBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: String?,
    var value: String?
) : BoProperty()

@Serializable
class UuidBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: UUID?,
    var value: UUID?
) : BoProperty()