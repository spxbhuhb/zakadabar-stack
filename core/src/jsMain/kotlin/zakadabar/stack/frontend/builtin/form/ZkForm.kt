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

import toast
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.FormClasses.Companion.formClasses
import zakadabar.stack.frontend.builtin.form.fields.*
import zakadabar.stack.frontend.builtin.form.structure.Buttons
import zakadabar.stack.frontend.builtin.form.structure.Header
import zakadabar.stack.frontend.builtin.form.structure.Section
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.Strings
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.frontend.util.log
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

/**
 * Base class for DTO forms.
 */
open class ZkForm<T : RecordDto<T>> : ZkElement() {

    lateinit var dto: T
    lateinit var mode: FormMode

    var crud: ZkCrud<T>? = null
    var fieldGridTemplate: String = "150px 1fr"
    var fieldGridGap: String = "5px"

    var schema = lazy { dto.schema() }

    internal val fields = mutableListOf<FormField<*>>()

    // ----  Builder convenience functions --------

    init {
        className = formClasses.form
    }

    fun header(title: String) = Header(title)

    fun buttons() = Buttons(this@ZkForm)

    fun section(title: String, summary: String = "", builder: ZkElement.() -> Unit) =
        Section(title, summary, builder)

    fun fieldGridSection(title: String, summary: String = "", builder: ZkElement.() -> Unit) =
        Section(title, summary) { + fieldGrid { builder() } }

    fun select(kProperty0: KMutableProperty0<RecordId<*>>, sortOptions: Boolean = true, options: suspend () -> List<Pair<RecordId<*>, String>>): ValidatedRecordSelect<T> {
        val field = ValidatedRecordSelect(this@ZkForm, kProperty0, sortOptions, options)
        fields += field
        return field
    }

    fun textarea(kProperty0: KMutableProperty0<String>, builder: ZkElement.() -> Unit = { }): ValidatedTextArea<T> {
        val field = ValidatedTextArea(this@ZkForm, kProperty0)
        fields += field
        field.builder()
        return field
    }

    // ----  Customized build and property receiver  --------

    operator fun KMutableProperty0<RecordId<T>>.unaryPlus(): ZkElement {
        val field = ValidatedId<T>(this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<String>.unaryPlus(): ZkElement {
        val field = ValidatedString(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<String?>.unaryPlus(): ZkElement {
        val field = ValidatedOptString(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Double>.unaryPlus(): ZkElement {
        val field = ValidatedDouble(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    fun ifNotCreate(build: ZkElement.() -> Unit) {
        if (mode == FormMode.Create) return
        build()
    }

    fun fieldGrid(build: ZkElement.() -> Unit) =
        grid(style = "grid-template-columns: $fieldGridTemplate; gap: $fieldGridGap", build = build)

    fun KProperty0<*>.label() = this.name

    // ----  Validation and submit --------

    fun validate(): ValidityReport {
        val report = schema.value.validate()
        fields.forEach {
            it.onValidated(report)
        }
        return report
    }

    fun submit() {
        val report = validate()

        if (report.fails.isNotEmpty()) {
            toast(warning = true) { Strings.invalidFields + report.fails.map { it.key }.joinToString(", ") }
            console.log("==== Validation Report ====")
            report.fails.forEach {
                console.log("${it.key} = ${it.value}")
            }
            return
        }

        launch {
            try {
                when (mode) {
                    FormMode.Create -> {
                        val created = dto.create()
                        fields.forEach { it.onCreateSuccess(created) }
                        toast { Strings.createSuccess }
                    }
                    FormMode.Read -> {
                        // nothing to do here
                    }
                    FormMode.Update -> {
                        dto.update()
                        toast { Strings.updateSuccess }
                    }
                    FormMode.Delete -> {
                        dto.delete()
                        toast { Strings.deleteSuccess }
                    }
                }
            } catch (ex: Exception) {
                log(ex)
                when (mode) {
                    FormMode.Create -> toast(error = true) { Strings.createFail }
                    FormMode.Read -> Unit
                    FormMode.Update -> toast(error = true) { Strings.updateFail }
                    FormMode.Delete -> toast(error = true) { Strings.deleteFail }
                }
            }
        }
    }
}