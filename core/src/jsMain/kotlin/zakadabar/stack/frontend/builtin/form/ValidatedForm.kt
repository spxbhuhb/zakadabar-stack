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
import zakadabar.stack.frontend.builtin.form.FormClasses.Companion.formClasses
import zakadabar.stack.frontend.builtin.form.fields.*
import zakadabar.stack.frontend.builtin.form.structure.Buttons
import zakadabar.stack.frontend.builtin.form.structure.Header
import zakadabar.stack.frontend.builtin.form.structure.Section
import zakadabar.stack.frontend.builtin.util.toast
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.frontend.util.log
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

/**
 * Base class for DTO forms.
 */
open class ValidatedForm<T : RecordDto<T>>(
    val dto: T,
    val mode: FormMode,
    val crud: ZkCrud<T>? = null,
    @PublicApi
    val fieldGridTemplate: String = "150px 1fr",
    val fieldGridGap: String = "5px",
    val builder: ValidatedForm<T>.() -> Unit = { }
) : ZkElement() {

    private val schema = dto.schema()
    internal val fields = mutableListOf<FormField<*>>()

    // ----  Builder convenience functions --------

    init {
        className = formClasses.form
    }

    override fun init(): ZkElement {
        builder()
        return this
    }

    fun header(title: String) = Header(title)

    fun buttons() = Buttons(this@ValidatedForm)

    fun section(title: String, summary: String = "", builder: ZkElement.() -> Unit) =
        Section(title, summary, builder)

    fun select(kProperty0: KMutableProperty0<RecordId<*>>, sortOptions: Boolean = true, options: suspend () -> List<Pair<RecordId<*>, String>>): ValidatedRecordSelect<T> {
        val field = ValidatedRecordSelect(this@ValidatedForm, kProperty0, sortOptions, options)
        fields += field
        return field
    }

    fun textarea(kProperty0: KMutableProperty0<String>, builder: ZkElement.() -> Unit = { }): ValidatedTextArea<T> {
        val field = ValidatedTextArea(this@ValidatedForm, kProperty0)
        fields += field
        field.builder()
        return field
    }

    // ----  Customized build and property receiver  --------

    operator fun KProperty0<RecordId<T>>.unaryPlus(): ZkElement {
        val field = ValidatedId<T>(this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<String>.unaryPlus(): ZkElement {
        val field = ValidatedString(this@ValidatedForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Double>.unaryPlus(): ZkElement {
        val field = ValidatedDouble(this@ValidatedForm, this)
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
                    FormMode.Create -> {
                        val created = dto.create()
                        fields.forEach { it.onCreateSuccess(created) }
                        toast { "create.success" }
                    }
                    FormMode.Read -> {
                        // nothing to do here
                    }
                    FormMode.Update -> {
                        dto.update()
                        toast { "update.success" }
                    }
                    FormMode.Delete -> {
                        dto.delete()
                        toast { "delete.success" }
                    }
                }
            } catch (ex: Exception) {
                log(ex)
                when (mode) {
                    FormMode.Create -> toast { "create.failed" }
                    FormMode.Read -> Unit
                    FormMode.Update -> toast { "update.failed" }
                    FormMode.Delete -> toast { "delete.failed" }
                }
            }
        }
    }
}