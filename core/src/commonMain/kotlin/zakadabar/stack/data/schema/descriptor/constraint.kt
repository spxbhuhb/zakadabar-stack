/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema.descriptor

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.stack.data.util.InstantAsStringSerializer

@Serializable
sealed class ConstraintDto {
    abstract val type: ConstraintType
}

@Serializable
class ConstraintBooleanDto(
    override val type: ConstraintType,
    val value: Boolean
) : ConstraintDto()

@Serializable
class ConstraintDoubleDto(
    override val type: ConstraintType,
    val value: Double?
) : ConstraintDto()

@Serializable
class ConstraintInstantDto(
    override val type: ConstraintType,
    @Serializable(InstantAsStringSerializer::class)
    val value: Instant?
) : ConstraintDto()

@Serializable
class ConstraintIntDto(
    override val type: ConstraintType,
    val value: Int?
) : ConstraintDto()

@Serializable
class ConstraintLongDto(
    override val type: ConstraintType,
    val value: Long?
) : ConstraintDto()

@Serializable
class ConstraintStringDto(
    override val type: ConstraintType,
    val value: String?
) : ConstraintDto()