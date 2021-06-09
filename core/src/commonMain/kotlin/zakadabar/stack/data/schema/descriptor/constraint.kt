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