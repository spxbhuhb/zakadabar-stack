/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.data.schema.descriptor

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId
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
class EntityIdBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    val kClassName: String,
    var defaultValue: EntityId<BaseBo>?,
    var value: EntityId<BaseBo>?
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
class BaseBoBoProperty(
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
class ListBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    val kClassName: String?
) : BoProperty()

@Serializable
class InstantBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: Instant?,
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
class LocalDateBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: LocalDate?,
    var value: LocalDate?
) : BoProperty()

@Serializable
class LocalDateTimeBoProperty(
    override val name: String,
    override val optional: Boolean,
    override var constraints: List<BoConstraint>,
    var defaultValue: LocalDateTime?,
    var value: LocalDateTime?
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