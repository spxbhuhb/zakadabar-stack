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
package zakadabar.samples.theperfectform.frontend.form

import zakadabar.samples.theperfectform.data.FormDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.frontend.builtin.util.NYI
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.util.launch
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

open class ValidatedForm<T : RecordDto<T>>(val dto: T) : ComplexElement() {

    private val schema = dto.schema()
    private val fields = mutableListOf<FormField<*>>()

    operator fun KProperty0<String>.unaryPlus() {
        val field = ValidatedString(this@ValidatedForm, this)
        fields += field
        this@ValidatedForm += field
    }

    operator fun KProperty1<RecordDto<*>, String>.unaryPlus() {

    }

    fun validate() {
        val report = schema.validate()

        println("validation result:")
        println(report.dump())

        fields.forEach {
            it.validated(report.fails[it.prop])
        }
    }

}

class PerfectForm(dto: FormDto) : ValidatedForm<FormDto>(dto) {

    val columns = build {
        col {
            label = ize
            field = FormDto::textField
            dataFunc = { dto: FormDto -> dto.textField }
            renderer = { }
        }
    }

    override fun init(): ValidatedForm<FormDto> {

        this build {
            + NYI("header")
            + NYI("frame")
        }

        launch {
            setData(FormDto.all())
        }

        this build {
            + dto::textField

            val a0 = dto::textField
            a0.get()

            // definicio
            val a1 = FormDto::textField

            // runtime
            val list = emptyList<FormDto>()

            list.forEach {
                + a1.get(it)
            }
        }

        return this
    }

    private fun setData(all: List<FormDto>) {
        TODO("Not yet implemented")
    }

}