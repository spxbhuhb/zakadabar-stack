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
package zakadabar.stack.data.schema.entries

import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoPropertyConstraint
import zakadabar.stack.data.schema.DtoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.ConstraintBooleanDto
import zakadabar.stack.data.schema.descriptor.ConstraintType
import zakadabar.stack.data.schema.descriptor.PropertyDto
import zakadabar.stack.data.schema.descriptor.RecordIdPropertyDto
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0

class RecordIdDtoSchemaEntry<T : Any>(
    val kClass: KClass<T>,
    val kProperty: KMutableProperty0<RecordId<T>>
) : DtoSchemaEntry<RecordId<T>> {

    var defaultValue: RecordId<T> = EmptyRecordId()

    private val rules = mutableListOf<DtoPropertyConstraint<RecordId<*>>>()

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }

    inner class Empty(@PublicApi val validValue: Boolean) : DtoPropertyConstraint<RecordId<*>> {

        override fun validate(value: RecordId<*>, report: ValidityReport) {
            if (value.isEmpty() != validValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintBooleanDto(ConstraintType.Empty, validValue)

    }

    @PublicApi
    infix fun empty(validValue: Boolean): RecordIdDtoSchemaEntry<T> {
        rules += Empty(validValue)
        return this
    }

    @PublicApi
    infix fun default(value: RecordId<T>): RecordIdDtoSchemaEntry<T> {
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
    override fun toPropertyDto() = RecordIdPropertyDto(
        kProperty.name,
        isOptional(),
        emptyList(),
        kClass.simpleName!!,
        defaultValue as RecordId<DtoBase>,
        kProperty.get() as RecordId<DtoBase>
    )


}