/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema.validations

import zakadabar.stack.data.schema.ValidationRule
import zakadabar.stack.data.schema.ValidationRuleList
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptStringValidationRuleList(val kProperty: KMutableProperty0<String?>) : ValidationRuleList<String?> {

    var defaultValue: String? = null

    private val rules = mutableListOf<ValidationRule<String?>>()

    inner class Max(@PublicApi val limit: Int) : ValidationRule<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value != null && value.length > limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = StringValidationIntDto(ValidationType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Int) : ValidationRule<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value != null && value.length < limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = StringValidationIntDto(ValidationType.Min, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: String) : ValidationRule<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = StringValidationStringDto(ValidationType.NotEquals, invalidValue)

    }


    inner class Blank(@PublicApi val validValue: Boolean) : ValidationRule<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value?.isBlank() != validValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = StringValidationBooleanDto(ValidationType.Blank, validValue)

    }

    @PublicApi
    infix fun max(limit: Int): OptStringValidationRuleList {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Int): OptStringValidationRuleList {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: String): OptStringValidationRuleList {
        rules += NotEquals(invalidValue)
        return this
    }

    @PublicApi
    infix fun blank(blank: Boolean): OptStringValidationRuleList {
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
    infix fun default(value: String?): OptStringValidationRuleList {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

    override fun push(dto: PropertyDto) {
        require(dto is OptStringPropertyDto)
        kProperty.set(dto.value)
    }

    override fun toPropertyDto() = OptStringPropertyDto(
        kProperty.name,
        rules.map { it.toValidationDto() },
        defaultValue,
        kProperty.get()
    )
}