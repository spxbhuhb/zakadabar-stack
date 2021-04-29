/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.stack.data.util.InstantAsStringSerializer

@Serializable
sealed class ValidationDto {
    abstract val type: ValidationType
}

@Serializable
class DoubleValidationDto(
    override val type: ValidationType,
    val value: Double?
) : ValidationDto()

@Serializable
class InstantValidationDto(
    override val type: ValidationType,
    @Serializable(InstantAsStringSerializer::class)
    val value: Instant?
) : ValidationDto()

@Serializable
class IntValidationDto(
    override val type: ValidationType,
    val value: Int?
) : ValidationDto()

@Serializable
class LongValidationDto(
    override val type: ValidationType,
    val value: Long?
) : ValidationDto()

@Serializable
class StringValidationIntDto(
    override val type: ValidationType,
    val value: Int
) : ValidationDto()

@Serializable
class StringValidationStringDto(
    override val type: ValidationType,
    val value: String?
) : ValidationDto()

@Serializable
class StringValidationBooleanDto(
    override val type: ValidationType,
    val value: Boolean
) : ValidationDto()