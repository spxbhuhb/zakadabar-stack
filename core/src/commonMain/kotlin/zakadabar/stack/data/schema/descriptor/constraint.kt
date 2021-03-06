/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema.descriptor

import kotlinx.datetime.Instant
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
    abstract val type: BoConstraintType
}

@Serializable
class BooleanBoConstraint(
    override val type: BoConstraintType,
    val value: Boolean
) : BoConstraint()

/**
 * A custom constraint, typically implemented with a code specific to
 * the given BO.
 *
 * @param  type  Type of the constraint, [BoConstraintType.Custom].
 * @param  name  Name of the constraint, anything that identifies the check.
 * @param  value Source code of the custom check (optional).
 */
@Serializable
class CustomBoConstraint(
    override val type: BoConstraintType = BoConstraintType.Custom,
    val name : String,
    val value : String? = null
) : BoConstraint()

@Serializable
class DoubleBoConstraint(
    override val type: BoConstraintType,
    val value: Double?
) : BoConstraint()

@Serializable
class InstantBoConstraint(
    override val type: BoConstraintType,
    val value: Instant?
) : BoConstraint()

@Serializable
class IntBoConstraint(
    override val type: BoConstraintType,
    val value: Int?
) : BoConstraint()

@Serializable
class LongBoConstraint(
    override val type: BoConstraintType,
    val value: Long?
) : BoConstraint()

@Serializable
class StringBoConstraint(
    override val type: BoConstraintType,
    val value: String?
) : BoConstraint()