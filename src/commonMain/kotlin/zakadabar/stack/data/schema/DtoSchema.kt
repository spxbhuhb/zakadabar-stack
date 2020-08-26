/*
 * Copyright © 2020, Simplexion, Hungary
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
package zakadabar.stack.data.schema

import kotlin.reflect.KClass
import kotlin.reflect.KProperty0

open class DtoSchema {

    companion object {
        fun build(init: DtoSchema.() -> Unit): DtoSchema {
            val schema = DtoSchema()
            schema.init()
            return schema
        }

        val NO_VALIDATION = DtoSchema()
    }

    private val ruleLists = mutableMapOf<KProperty0<*>, ValidationRuleList<*>>()

    operator fun KProperty0<String>.unaryPlus(): StringValidationRuleList {
        val ruleList = StringValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

}

interface ValidationRuleList<T>

interface ValidationRule<T> {
    fun validate(value: String, report: ValidityReport)
}

class ValidityReport(
    val fails: MutableMap<KProperty0<*>, MutableList<KClass<*>>> = mutableMapOf()
) {
    fun fail(property: KProperty0<*>, validation: KClass<*>) {
        fails.getOrPut(property) { mutableListOf() } += validation
    }
}