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
package zakadabar.stack.frontend.builtin.form

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.fields.*
import zakadabar.stack.frontend.builtin.form.structure.Buttons
import zakadabar.stack.frontend.builtin.form.structure.Header
import zakadabar.stack.frontend.builtin.form.structure.Section
import zakadabar.stack.frontend.builtin.util.toast
import zakadabar.stack.frontend.elements.ZkBuilder
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.ZkPropertyReceiver
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.frontend.util.log
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

open class ValidatedForm<T : RecordDto<T>>(
    val dto: T,
    val crud: ZkCrud<T>,
    val mode: Mode
) : ZkElement(), ZkPropertyReceiver {

    enum class Mode {
        Create,
        Read,
        Update,
        Delete
    }

    private val schema = dto.schema()
    internal val fields = mutableListOf<FormField<*>>()

    // ----  Builder convenience functions --------

    fun header(title: String) =
        Header(title)

    fun buttons() =
        Buttons(this)

    fun section(title: String, summary: String = "", builder: ZkBuilder.() -> Unit) =
        Section(title, summary, builder)

    fun select(kProperty0: KMutableProperty0<RecordId<*>>, sortOptions: Boolean = true, options: suspend () -> List<Pair<RecordId<*>, String>>): ValidatedRecordSelect<T> {
        val field = ValidatedRecordSelect(this, kProperty0, sortOptions, options)
        fields += field
        return field
    }

    // ----  Customized build and property receiver  --------

    override infix fun build(build: ZkBuilder.() -> Unit): ZkElement {
        ZkBuilder.propertyReceiver = this
        ZkBuilder(this, element).build()
        ZkBuilder.propertyReceiver = null
        return this
    }

    override infix fun launchBuild(build: suspend ZkBuilder.() -> Unit): ZkElement {
        launch {
            ZkBuilder.propertyReceiver = this
            ZkBuilder(this, element).build()
            ZkBuilder.propertyReceiver = null
        }
        return this
    }

    override fun add(kProperty0: KProperty0<RecordId<T>>): ZkElement {
        val field = ValidatedId<T>(kProperty0)
        fields += field
        return field
    }

    override fun add(kProperty0: KMutableProperty0<String>): ZkElement {
        val field = ValidatedString(this, kProperty0)
        fields += field
        return field
    }

    override fun add(kProperty0: KMutableProperty0<Double>): ZkElement {
        val field = ValidatedDouble(this, kProperty0)
        fields += field
        return field
    }


    // ----  Validation and submit --------

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
                log(ex)
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