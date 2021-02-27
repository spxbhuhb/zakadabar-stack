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

import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.action.ActionDto
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.frontend.builtin.form.fields.*
import zakadabar.stack.frontend.builtin.form.structure.ZkFormButtons
import zakadabar.stack.frontend.builtin.form.structure.ZkFormSection
import zakadabar.stack.frontend.builtin.toast.toast
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.CoreStrings
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.frontend.util.log
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

/**
 * Base class for DTO forms.
 *
 * @property  autoLabel  When true labels are automatically added. When false they are not.
 */
open class ZkForm<T : DtoBase> : ZkElement() {

    lateinit var dto: T
    lateinit var mode: ZkFormMode

    var title: String = ""
    var titleBar: ZkFormTitleBar? = null

    var autoLabel = true

    @PublicApi
    var fieldGridTemplate: String = "150px 1fr"

    var openUpdate: ((dto: T) -> Unit)? = null

    /**
     * A function to be called when execution of the form operation has a result.
     * This means that the server sent a response which is successful at HTTP level.
     * However, the response may indicate an error.
     */
    var onExecuteResult: ((resultDto: DtoBase) -> Unit)? = null

    var schema = lazy { dto.schema() }

    internal val fields = mutableListOf<ZkFieldBase<T, *>>()

    // ----  Builder convenience functions --------

    override fun init(): ZkElement {
        classList += ZkFormStyles.outerContainer
        buildTitleBar()?.let { + it }
        return this
    }

    private fun buildTitleBar(): ZkFormTitleBar? {

        // this check lets the extending class to define it's own title bar
        // TODO think about this, might be better to make buildTitleBar protected

        if (titleBar == null) {
            titleBar = ZkFormTitleBar {
                title = this@ZkForm.title
            }
        }

        return titleBar
    }

    fun buttons() = ZkFormButtons(this@ZkForm)

    fun section(title: String, summary: String = "", fieldGrid: Boolean = true, builder: ZkElement.() -> Unit): ZkFormSection {
        return if (fieldGrid) {
            ZkFormSection(title, summary) { + fieldGrid { builder() } }
        } else {
            ZkFormSection(title, summary, builder)
        }
    }

    @Deprecated("use simple section instead", ReplaceWith("section(title, summary, builder)"))
    fun fieldGridSection(title: String, summary: String = "", builder: ZkElement.() -> Unit) =
        section(title, summary, true, builder)

    fun select(
        kProperty0: KMutableProperty0<RecordId<*>>,
        sortOptions: Boolean = true,
        options: suspend () -> List<Pair<RecordId<*>, String>>
    ): ZkRecordSelectField<T> {
        val field = ZkRecordSelectField(this@ZkForm, kProperty0, sortOptions, options)
        fields += field
        return field

    }

    fun select(
        kProperty0: KMutableProperty0<RecordId<*>?>,
        sortOptions: Boolean = true,
        options: suspend () -> List<Pair<RecordId<*>, String>>
    ): ZkOptRecordSelectField<T> {

        val field = ZkOptRecordSelectField(this@ZkForm, kProperty0, sortOptions, options)
        fields += field
        return field

    }

    fun select(
        kProperty0: KMutableProperty0<String>,
        options: List<String>,
        sortOptions: Boolean = true
    ): ZkSelectField<T> {

        val field = ZkSelectField(this@ZkForm, kProperty0, sortOptions, suspend { options.map { Pair(it, it) } })
        fields += field
        return field

    }

    fun select(
        kProperty0: KMutableProperty0<String?>,
        options: List<String>,
        sortOptions: Boolean = true
    ): ZkOptSelectField<T> {

        val field = ZkOptSelectField(this@ZkForm, kProperty0, sortOptions, suspend { options.map { Pair(it, it) } })
        fields += field
        return field

    }

    fun textarea(kProperty0: KMutableProperty0<String>, builder: ZkElement.() -> Unit = { }): ZkTextAreaField<T> {
        val field = ZkTextAreaField(this@ZkForm, kProperty0)
        fields += field
        field.builder()
        return field
    }

    fun ifNotCreate(build: ZkElement.() -> Unit) {
        if (mode == ZkFormMode.Create) return
        build()
    }

    fun fieldGrid(build: ZkElement.() -> Unit) =
        grid(style = "grid-template-columns: $fieldGridTemplate; gap: 0", build = build)

    // ----  Customized build and property receiver  --------

    operator fun KMutableProperty0<RecordId<T>>.unaryPlus(): ZkElement {
        val field = ZkIdField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<String>.unaryPlus(): ZkElement {
        val field = ZkStringField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<String?>.unaryPlus(): ZkElement {
        val field = ZkOptStringField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Double>.unaryPlus(): ZkElement {
        val field = ZkDoubleField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    operator fun KMutableProperty0<Boolean>.unaryPlus(): ZkElement {
        val field = ZkBooleanField(this@ZkForm, this)
        + field
        fields += field
        return field
    }

    // ----  Validation and submit --------

    fun validate(): ValidityReport {
        val report = schema.value.validate()
        fields.forEach {
            it.onValidated(report)
        }
        return report
    }

    fun submit() {

        val invalid = mutableListOf<String>()

        // submit marks all fields touched to show all invalid fields for the user
        fields.forEach {
            it.touched = true
            if (! it.valid) invalid += it.propName
        }

        val report = validate()

        report.fails.keys.forEach { invalid += it }

        if (invalid.isNotEmpty()) {
            toast(warning = true) { CoreStrings.invalidFields + invalid.joinToString(", ") } // TODO translation
            return
        }

        launch {
            try {
                when (mode) {
                    ZkFormMode.Create -> {
                        val created = (dto as RecordDto<*>).create() as RecordDto<*>
                        fields.forEach { it.onCreateSuccess(created) }
                        toast { CoreStrings.createSuccess }
                    }
                    ZkFormMode.Read -> {
                        // nothing to do here
                    }
                    ZkFormMode.Update -> {
                        (dto as RecordDto<*>).update()
                        toast { CoreStrings.updateSuccess }
                    }
                    ZkFormMode.Delete -> {
                        (dto as RecordDto<*>).delete()
                        toast { CoreStrings.deleteSuccess }
                    }
                    ZkFormMode.Action -> {
                        val result = (dto as ActionDto<*>).execute() as DtoBase
                        onExecuteResult?.let { it(result) }
                        toast { CoreStrings.actionSuccess }
                    }
                    ZkFormMode.Query -> {
                        (dto as QueryDto<*>).execute()
                        // TODO do something here with the result
                    }
                }
            } catch (ex: Exception) {
                log(ex)
                when (mode) {
                    ZkFormMode.Create -> toast(error = true) { CoreStrings.createFail }
                    ZkFormMode.Read -> Unit
                    ZkFormMode.Update -> toast(error = true) { CoreStrings.updateFail }
                    ZkFormMode.Delete -> toast(error = true) { CoreStrings.deleteFail }
                    ZkFormMode.Action -> toast(error = true) { CoreStrings.actionFail }
                    ZkFormMode.Query -> toast(error = true) { CoreStrings.queryFail }
                }
            }
        }
    }
}