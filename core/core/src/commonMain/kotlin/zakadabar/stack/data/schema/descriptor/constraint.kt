/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data.schema.descriptor

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

enum class BoConstraintType {
    Min,
    Max,
    Blank,
    Empty,
    NotEquals,
    After,
    Before,
    Format,
    Custom
}

@Serializable
sealed class BoConstraint {
    abstract val constraintType: BoConstraintType
}

@Serializable
class BooleanBoConstraint(
    override val constraintType: BoConstraintType,
    val value: Boolean
) : BoConstraint()

/**
 * A custom constraint, typically implemented with a code specific to
 * the given BO.
 *
 * @param  constraintType  Type of the constraint, [BoConstraintType.Custom].
 * @param  name  Name of the constraint, anything that identifies the check.
 * @param  value Source code of the custom check (optional).
 */
@Serializable
class CustomBoConstraint(
    override val constraintType: BoConstraintType = BoConstraintType.Custom,
    val name : String,
    val value : String? = null
) : BoConstraint()

@Serializable
class DoubleBoConstraint(
    override val constraintType: BoConstraintType,
    val value: Double?
) : BoConstraint()

@Serializable
class InstantBoConstraint(
    override val constraintType: BoConstraintType,
    val value: Instant?
) : BoConstraint()

@Serializable
class IntBoConstraint(
    override val constraintType: BoConstraintType,
    val value: Int?
) : BoConstraint()

@Serializable
class LocalDateBoConstraint(
    override val constraintType: BoConstraintType,
    val value: LocalDate?
) : BoConstraint()

@Serializable
class LocalDateTimeBoConstraint(
    override val constraintType: BoConstraintType,
    val value: LocalDateTime?
) : BoConstraint()

@Serializable
class LongBoConstraint(
    override val constraintType: BoConstraintType,
    val value: Long?
) : BoConstraint()

@Serializable
class StringBoConstraint(
    override val constraintType: BoConstraintType,
    val value: String?
) : BoConstraint()