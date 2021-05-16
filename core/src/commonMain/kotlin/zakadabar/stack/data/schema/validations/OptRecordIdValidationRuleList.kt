/*
 * Copyright © 2020, Simplexion, Hungary and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.stack.data.schema.validations

import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidationRule
import zakadabar.stack.data.schema.ValidationRuleList
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0

class OptRecordIdValidationRuleList<T : Any>(
    val kClass: KClass<T>,
    val kProperty: KMutableProperty0<RecordId<T>?>
) : ValidationRuleList<RecordId<*>?> {

    var defaultValue: RecordId<T>? = null

    private val rules = mutableListOf<ValidationRule<RecordId<T>?>>()

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }

    inner class Empty(@PublicApi val validValue: Boolean) : ValidationRule<RecordId<T>?> {

        override fun validate(value: RecordId<T>?, report: ValidityReport) {
            if (value?.isEmpty() != validValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = RecordIdValidationBooleanDto(ValidationType.Empty, validValue)

    }

    @PublicApi
    infix fun empty(validValue: Boolean): OptRecordIdValidationRuleList<*> {
        rules += Empty(validValue)
        return this
    }

    @PublicApi
    infix fun default(value: RecordId<T>?): OptRecordIdValidationRuleList<*> {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = false

    override fun push(dto: PropertyDto) {
        require(dto is RecordIdPropertyDto)
        @Suppress("UNCHECKED_CAST") // FIXME clarify record id type erasure
        kProperty.set(dto.value as RecordId<T>)
    }

    @Suppress("UNCHECKED_CAST") // should work, lost in generics hell
    override fun toPropertyDto() = OptRecordIdPropertyDto(
        kProperty.name,
        emptyList(),
        kClass.simpleName!!,
        defaultValue as RecordId<DtoBase>,
        kProperty.get() as RecordId<DtoBase>
    )


}