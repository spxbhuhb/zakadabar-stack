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

import zakadabar.stack.data.schema.ValidationRuleList
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.dto.OptEnumPropertyDto
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptEnumValidationRuleList<E : Enum<E>>(
    val kProperty: KMutableProperty0<E?>,
    val values : Array<E>
) : ValidationRuleList<E> {

    var defaultValue: E? = null

    override fun validate(report: ValidityReport) {}

    @PublicApi
    infix fun default(value: E): OptEnumValidationRuleList<E> {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

    override fun toPropertyDto() = OptEnumPropertyDto(
        kProperty.name,
        emptyList(),
        values.map { it.name },
        defaultValue?.name,
        kProperty.get()?.name
    )

}