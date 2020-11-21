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
package zakadabar.demo.frontend.form

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.util.toast
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

open class ValidatedForm<T : RecordDto<T>>(val dto: T, val mode: Mode) : ZkElement() {

    enum class Mode {
        Create,
        Read,
        Update,
        Delete
    }

    private val schema = dto.schema()
    private val fields = mutableListOf<FormField<*>>()

    operator fun KProperty0<RecordId<T>>.unaryPlus() {
        val field = ValidatedId<T>(this)
        fields += field
        this@ValidatedForm += field
    }

    operator fun KMutableProperty0<String>.unaryPlus() {
        val field = ValidatedString(this@ValidatedForm, this)
        fields += field
        this@ValidatedForm += field
    }

    operator fun KMutableProperty0<Double>.unaryPlus() {
        val field = ValidatedDouble(this@ValidatedForm, this)
        fields += field
        this@ValidatedForm += field
    }

    fun validate(): ValidityReport {
        val report = schema.validate()
        fields.forEach {
            it.onValidated(report)
        }
        return report
    }

    fun submit() {
        val report = validate()

        if (report.fails.isNotEmpty()) {
            toast { "validation.failed" }
            return
        }

        launch {
            try {
                when (mode) {
                    Mode.Create -> {
                        dto.create()
                        toast { "create.success" }
                    }
                    Mode.Read -> {
                        // nothing to do here
                    }
                    Mode.Update -> {
                        dto.update()
                        toast { "update.success" }
                    }
                    Mode.Delete -> {
                        dto.delete()
                        toast { "delete.success" }
                    }
                }
            } catch (ex: Exception) {
                when (mode) {
                    Mode.Create -> toast { "create.failed" }
                    Mode.Read -> Unit
                    Mode.Update -> toast { "update.failed" }
                    Mode.Delete -> toast { "delete.failed" }
                }
            }
        }
    }
}