/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema.entries

import zakadabar.stack.data.schema.DtoPropertyConstraint
import zakadabar.stack.data.schema.DtoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptStringDtoSchemaEntry(val kProperty: KMutableProperty0<String?>) : DtoSchemaEntry<String?> {

    var defaultValue: String? = null

    private val rules = mutableListOf<DtoPropertyConstraint<String?>>()

    inner class Max(@PublicApi val limit: Int) : DtoPropertyConstraint<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value != null && value.length > limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintIntDto(ConstraintType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Int) : DtoPropertyConstraint<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value != null && value.length < limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintIntDto(ConstraintType.Min, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: String) : DtoPropertyConstraint<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintStringDto(ConstraintType.NotEquals, invalidValue)

    }


    inner class Blank(@PublicApi val validValue: Boolean) : DtoPropertyConstraint<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value?.isBlank() != validValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintBooleanDto(ConstraintType.Blank, validValue)

    }

    @PublicApi
    infix fun max(limit: Int): OptStringDtoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Int): OptStringDtoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: String): OptStringDtoSchemaEntry {
        rules += NotEquals(invalidValue)
        return this
    }

    @PublicApi
    infix fun blank(blank: Boolean): OptStringDtoSchemaEntry {
        rules += Blank(blank)
        return this
    }

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }

    @PublicApi
    infix fun default(value: String?): OptStringDtoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

    override fun push(dto: PropertyDto) {
        require(dto is StringPropertyDto)
        kProperty.set(dto.value)
    }

    override fun toPropertyDto() = StringPropertyDto(
        kProperty.name,
        isOptional(),
        rules.map { it.toValidationDto() },
        defaultValue,
        kProperty.get()
    )
}