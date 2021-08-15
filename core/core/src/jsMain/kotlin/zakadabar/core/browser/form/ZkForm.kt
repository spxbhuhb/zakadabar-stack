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
package zakadabar.core.browser.form

import kotlinx.browser.document
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.w3c.dom.HTMLElement
import zakadabar.core.data.BaseBo
import zakadabar.core.data.ActionBo
import zakadabar.core.data.Secret
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.QueryBo
import zakadabar.core.schema.BoSchema
import zakadabar.core.schema.ValidityReport
import zakadabar.core.exception.DataConflict
import zakadabar.core.browser.application.application
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.ZkElementState
import zakadabar.core.browser.crud.ZkCrudEditor
import zakadabar.core.browser.field.*
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.titlebar.ZkAppTitleProvider
import zakadabar.core.browser.titlebar.ZkLocalTitleProvider
import zakadabar.core.browser.toast.ZkToast
import zakadabar.core.browser.toast.toastDanger
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.browser.toast.toastWarning
import zakadabar.core.resource.css.ZkCssStyleRule
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.log
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.localizedStrings
import zakadabar.core.util.PublicApi
import zakadabar.core.util.UUID
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * Base class for BO forms.
 *
 * @property  setAppTitle   When true (default) the app title bar is set for the table. Function [setAppTitleBar] adds the title bar.
 * @property  addLocalTitle When true, add a local title bar. Default is false.
 * @property  titleText     Title text to show in the title bar. Used when [titleElement] is not set.
 * @property  titleElement  The element of the title.
 * @property  autoLabel  When true labels are automatically added. When false they are not.
 */
open class ZkForm<T : BaseBo>(
    element: HTMLElement = document.createElement("div") as HTMLElement,
) : ZkElement(element), ZkCrudEditor<T>, ZkAppTitleProvider, ZkLocalTitleProvider, ZkFieldContext {

    private var _bo: T? = null

    override var bo: T
        get() = _bo ?: throw IllegalStateException("bo is not initialized yet")
        set(value) {
            _bo = value
            schema = value.schema()
        }

    override var schema = BoSchema.Companion.NO_VALIDATION

    override lateinit var mode: ZkElementMode

    override var setAppTitle = true
    override var addLocalTitle = false
    override var titleText: String? = null
    override var titleElement: ZkAppTitle? = null

    var autoLabel = true

    @PublicApi
    var fieldGridColumnTemplate: String = "minmax(150px, max-content) minmax(150px,1fr)"

    @PublicApi
    var fieldGridRowTemplate: String = "max-content"

    override var openUpdate: ((bo: T) -> Unit)? = null

    /**
     * A function to be called when execution of the form operation has a BO result.
     * This means that the server sent a response which is successful at HTTP level.
     * However, the response may indicate an error.
     *
     * **Not executed when the result is not a Business Object.**
     */
    var onExecuteResult: ((resultBo: BaseBo) -> Unit)? = null

    /**
     * Called when the user clicks on the back button.
     */
    override var onBack = { application.back() }

    val fields = mutableListOf<ZkFieldBase<*>>()

    /**
     * False until the first time the user clicks on the submit button.
     * Before first submit the list of invalid fields are not shown.
     * After the first submit try the list of invalid fields are shown
     * (if there are any) and is updated automatically.
     */
    var submitTouched = false

    /**
     * The toast that shows the message that there are invalid values.
     * Automatically goes hidden if validation is successful.
     */
    var invalidToast: ZkToast? = null

    /**
     * The submit button element of the form. Set by [ZkFormButtons] which is
     * added by [build] or [buttons]. This is here so we can enable / disable
     * the button depending on the validity of the form.
     */
    var submitButton: ZkElement? = null

    /** An element that is show when when the form is submitting. */
    var progressIndicator: ZkElement? = null

    /**
     * Container to display list of the invalid fields. Filled automatically when
     * present.
     */
    var invalidFields: ZkInvalidFieldList? = null

    /**
     * When true the form will go back one page after create is successful.
     */
    var goBackAfterCreate: Boolean = true

    // -------------------------------------------------------------------------
    //  ZkFieldBackend
    // -------------------------------------------------------------------------

    override val readOnly
        get() = mode == ZkElementMode.Read

    override val useShadow
        get() = mode in listOf(ZkElementMode.Create, ZkElementMode.Action, ZkElementMode.Query)

    override val addLabel = true

    override val dense = false

    // -------------------------------------------------------------------------
    //  Lifecycle functions
    // -------------------------------------------------------------------------

    /**
     * Called by [onCreate] to configure the table before building it.
     * This is the place to add columns, switch on and off features.
     */
    open fun onConfigure() {

    }

    override fun onCreate() {
        super.onCreate()
        onConfigure()
        classList += ZkFormStyles.outerContainer

        if (addLocalTitle) {
            + buildLocalTitleBar()?.let { it marginBottom 10 }
        }
    }

    override fun onResume() {
        super.onResume()
        setAppTitleBar()
    }

    // -------------------------------------------------------------------------
    //  Validation and submit
    // ------------------------------------------------------------------------

    /**
     * Parameter-less version of submit for [ZkFieldContext].
     */
    override fun validate() {
        validate(submit = false)
    }

    /**
     * Validates the BO with the schema. Validation occurs at every
     * change. When the user did not touch a field yet a validation
     * fail may not be displayed for that field.
     *
     * @return true if the BO is valid, false otherwise
     */
    open fun validate(submit: Boolean = false): Boolean {
        if (submit) {
            // submit marks all fields touched to show all invalid fields for the user
            fields.forEach {
                it.touched = true
            }
            submitTouched = true
        }

        val report = schema.validate(mode == ZkElementMode.Create)

        if (submit && report.fails.isNotEmpty()) {
            println("${this::class.simpleName} validation fail:")
            println(report.dump())
        }

        fields.forEach {
            it.onValidated(report)
        }

        val invalid = invalidTouchedFields(report)

        if (! submitTouched || invalid.isEmpty()) {
            invalidFields?.hide()
            invalidToast?.dispose()
        }

        if (submitTouched && invalid.isNotEmpty()) {
            invalidFields?.show(invalid)
        }

        if (submit && (invalid.isNotEmpty() || report.fails.isNotEmpty())) {
            onInvalidSubmit()
            return false
        }

        return true
    }

    open fun invalidTouchedFields(report: ValidityReport): List<ZkFieldBase<*>> {
        val invalid = mutableListOf<ZkFieldBase<*>>()

        report.fails.keys.forEach { propName ->
            val field = fields.firstOrNull { it.propName == propName } ?: return@forEach
            if (field.touched) {
                invalid += field
            }
        }

        // it is possible that a field is not touched but invalid by default
        // for example a mandatory name which cannot be blank but starts with blank

        fields.forEach {
            if (it.touched && ! it.valid) invalid += it
        }

        // using distinct because report and field.invalid can add the same field twice

        return invalid.distinct()
    }

    /**
     * Callback to finalize the BO before submit starts. Called before validation.
     * Default implementation does nothing.
     *
     * This is a suspend function that allows the form perform validations that require
     * backend calls.
     */
    open suspend fun onSubmitStart() {

    }

    fun submit() {

        submitButton?.hide()
        progressIndicator?.show()

        io {
            try {

                onSubmitStart()

                if (! validate(true)) return@io

                when (mode) {
                    ZkElementMode.Create -> {
                        val created = (bo as EntityBo<*>).create() as EntityBo<*>
                        fields.forEach { it.onCreateSuccess(created) }
                        @Suppress("UNCHECKED_CAST")
                        onCreateSuccess(created as T)

                        if (goBackAfterCreate) {
                            onBack()
                        } else {
                            resetTouched()
                        }
                    }
                    ZkElementMode.Read -> {
                        // nothing to do here
                    }
                    ZkElementMode.Update -> {
                        (bo as EntityBo<*>).update()
                        resetTouched()
                    }
                    ZkElementMode.Delete -> {
                        (bo as EntityBo<*>).delete()
                        resetTouched()
                    }
                    ZkElementMode.Action -> {
                        val result = (bo as ActionBo<*>).execute()
                        if (result is BaseBo) {
                            onExecuteResult?.let { it(result) }
                        }
                        resetTouched()
                    }
                    ZkElementMode.Query -> {
                        (bo as QueryBo<*>).execute()
                        // TODO do something here with the result
                    }
                    ZkElementMode.Other -> {

                    }
                }

                onSubmitSuccess()

            } catch (ex: DataConflict) {

                toastDanger { localizedStrings[ex.message] }

            } catch (ex: Exception) {

                log(ex)
                onSubmitError(ex)

            } finally {

                submitButton?.show()
                progressIndicator?.hide()

            }
        }
    }

    private fun resetTouched() {
        submitTouched = false
        fields.forEach {
            it.touched = false
        }
    }

    /**
     * Called when the user tries to submit an invalid form.
     *
     * Default implementation shows a toast with a message.
     */
    open fun onInvalidSubmit() {
        invalidToast = toastWarning(hideAfter = 3000) { localizedStrings.invalidFieldsToast }
    }

    /**
     * Called after a create submit succeeded.
     *
     * @param  created  The BO that the server sent back.
     */
    open fun onCreateSuccess(created: T) {

    }

    /**
     * Called when submitting the form returns with a success.
     *
     * Default implementation shows a toast.
     */
    open fun onSubmitSuccess() {
        when (mode) {
            ZkElementMode.Create -> toastSuccess { localizedStrings.createSuccess }
            ZkElementMode.Read -> Unit
            ZkElementMode.Update -> toastSuccess { localizedStrings.updateSuccess }
            ZkElementMode.Delete -> toastSuccess { localizedStrings.deleteSuccess }
            ZkElementMode.Action -> toastSuccess { localizedStrings.actionSuccess }
            ZkElementMode.Query -> Unit
            ZkElementMode.Other -> toastSuccess { localizedStrings.actionSuccess }
        }
    }

    /**
     * Called when submitting the form returns with an error.
     *
     * Default implementation shows a toast.
     */
    open fun onSubmitError(ex: Exception) {
        when (mode) {
            ZkElementMode.Create -> toastDanger { localizedStrings.createFail }
            ZkElementMode.Read -> Unit
            ZkElementMode.Update -> toastDanger { localizedStrings.updateFail }
            ZkElementMode.Delete -> toastDanger { localizedStrings.deleteFail }
            ZkElementMode.Action -> toastDanger { localizedStrings.actionFail }
            ZkElementMode.Query -> toastDanger { localizedStrings.queryFail }
            ZkElementMode.Other -> toastDanger { localizedStrings.actionFail }
        }
    }

    // -------------------------------------------------------------------------
    //  Field builders
    // ------------------------------------------------------------------------

    /**
     * Create and add a form field.
     */
    open fun <T : ZkFieldBase<*>, PT : KProperty<*>> add(property: PT, function: (PT) -> T): T =
        function(property).also {
            + it
            fields += it
        }

    open fun build(title: String, createTitle: String = title, css: ZkCssStyleRule? = null, addButtons: Boolean = true, builder: () -> Unit) {
        this.titleText = if (mode == ZkElementMode.Create) createTitle else title

        // this lets build be called from an IO block after onResume ran
        if (lifeCycleState == ZkElementState.Resumed && setAppTitle) setAppTitleBar()

        + div(ZkFormStyles.contentContainer) {
            + column(ZkFormStyles.form) {
                css?.let { buildPoint.classList += it }

                builder()

                if (addButtons) {
                    + buttons()
                }
                + invalidFieldList()
            }
        }
    }

    fun buttons() = ZkFormButtons(this@ZkForm)

    open fun invalidFieldList() = ZkInvalidFieldList().also { invalidFields = it }.hide()

    fun section(title: String? = null, summary: String? = null, fieldGrid: Boolean = true, css: ZkCssStyleRule? = null, builder: ZkElement.() -> Unit): ZkFormSection {
        return if (fieldGrid) {
            ZkFormSection(title, summary, css) { + fieldGrid { builder() } }
        } else {
            ZkFormSection(title, summary, css) { + withoutFieldGrid { builder() } }
        }
    }

    fun withoutFieldGrid(build: ZkElement.() -> Unit) =
        div(build = build)

    fun fieldGrid(build: ZkElement.() -> Unit) =
        grid(style = "grid-template-columns: $fieldGridColumnTemplate; gap: 0; grid-auto-rows: $fieldGridRowTemplate", build = build)

    @Deprecated("EOL: 2021.8.1  -  use simple section instead", ReplaceWith("section(title, summary, builder)"))
    fun fieldGridSection(title: String, summary: String = "", css: ZkCssStyleRule? = null, builder: ZkElement.() -> Unit) =
        section(title, summary, true, css, builder)

    fun <T : EntityBo<T>> List<T>.by(field: (it: T) -> String) = map { it.id to field(it) }.sortedBy { it.second }

    fun select(kProperty0: KMutableProperty0<String>, label: String? = null, sortOptions: Boolean = true, options: List<String>): ZkStringSelectField {
        return add(kProperty0) {
            ZkStringSelectField(this@ZkForm, kProperty0).apply {
                fetch = suspend { options.map { Pair(it, it) } }
            }
        }
    }

    fun select(kProperty0: KMutableProperty0<String?>, options: List<String>): ZkOptStringSelectField {
        return add(kProperty0) {
            ZkOptStringSelectField(this@ZkForm, kProperty0).apply {
                fetch = suspend { options.map { Pair(it, it) } }
            }
        }
    }

    @Deprecated("Use '+' instead.")
    inline fun <reified E : Enum<E>> select(kProperty0: KMutableProperty0<E>): ZkEnumSelectField<E> {
        return (+ kProperty0)
    }

    @JsName("FormOptEnumSelect")
    @Deprecated("Use '+' instead.")
    inline fun <reified E : Enum<E>> select(kProperty0: KMutableProperty0<E?>): ZkOptEnumSelectField<E> {
        return (+ kProperty0)
    }

    fun textarea(kProperty0: KMutableProperty0<String>, builder: ZkTextAreaField.() -> Unit = { }): ZkTextAreaField {
        val field = ZkTextAreaField(this@ZkForm, kProperty0)
        fields += field
        field.builder()
        return field
    }

    fun textarea(kProperty0: KMutableProperty0<String?>, builder: ZkElement.() -> Unit = { }): ZkOptTextAreaField {
        val field = ZkOptTextAreaField(this@ZkForm, kProperty0)
        fields += field
        field.builder()
        return field
    }

    fun opt(kProperty0: KMutableProperty0<Boolean?>, trueText: String, falseText: String, builder: ZkOptBooleanField.() -> Unit = { }): ZkOptBooleanField {
        val options = listOf(true to trueText, false to falseText)
        val field = ZkOptBooleanField(this@ZkForm, kProperty0).also {
            it.fetch = { options }
        }
        fields += field
        field.builder()
        return field
    }

    fun constString(label: String, value: () -> String): ZkConstStringField {
        val field = ZkConstStringField(this@ZkForm, label, value())
        fields += field
        return field
    }

    /**
     * Field for a new secret (password). Instructs browsers not to fill the content with old password.
     */
    @Deprecated("EOL: 2021.8.1  -  use +bo::field newSecret true")
    fun newSecret(prop: KMutableProperty0<Secret>): ZkSecretField {
        val field = ZkSecretField(this@ZkForm, prop, newSecret = true)
        + field
        fields += field
        return field
    }

    /**
     * Field for a new secret (password). Instructs browsers not to fill the content with old password.
     */
    @Deprecated("EOL: 2021.8.1  -  use +bo::field newSecret true")
    fun newSecret(prop: KMutableProperty0<Secret?>): ZkOptSecretField {
        val field = ZkOptSecretField(this@ZkForm, prop, newSecret = true)
        + field
        fields += field
        return field
    }

    fun ifNotCreate(build: ZkElement.() -> Unit) {
        if (mode == ZkElementMode.Create) return
        build()
    }

    // -------------------------------------------------------------------------
    //  Property field builder shorthands
    // ------------------------------------------------------------------------

    operator fun KMutableProperty0<EntityId<T>>.unaryPlus(): ZkEntityIdField<T> =
        add(this) { prop ->
            ZkEntityIdField(this@ZkForm, prop).also {
                if (readOnly) it.hide()
            }
        }

    operator fun <ST : EntityBo<ST>> KMutableProperty0<EntityId<ST>>.unaryPlus(): ZkEntitySelectField<ST> =
        add(this) {
            ZkEntitySelectField(this@ZkForm, it)
        }

    operator fun <ST : EntityBo<ST>> KMutableProperty0<EntityId<ST>?>.unaryPlus(): ZkOptEntitySelectField<ST> =
        add(this) {
            ZkOptEntitySelectField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<String>.unaryPlus(): ZkStringField =
        add(this) {
            ZkStringField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<String?>.unaryPlus(): ZkOptStringField =
        add(this) {
            ZkOptStringField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Int>.unaryPlus(): ZkIntField =
        add(this) {
            ZkIntField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Int?>.unaryPlus(): ZkOptIntField =
        add(this) {
            ZkOptIntField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Long>.unaryPlus(): ZkLongField =
        add(this) {
            ZkLongField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Long?>.unaryPlus(): ZkOptLongField =
        add(this) {
            ZkOptLongField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Double>.unaryPlus(): ZkDoubleField =
        add(this) {
            ZkDoubleField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Double?>.unaryPlus(): ZkOptDoubleField =
        add(this) {
            ZkOptDoubleField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Boolean>.unaryPlus(): ZkBooleanField =
        add(this) {
            ZkBooleanField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Instant>.unaryPlus(): ZkInstantField =
        add(this) {
            ZkInstantField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Instant?>.unaryPlus(): ZkOptInstantField =
        add(this) {
            ZkOptInstantField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<LocalDate>.unaryPlus(): ZkLocalDateField =
        add(this) {
            ZkLocalDateField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<LocalDate?>.unaryPlus(): ZkOptLocalDateField =
        add(this) {
            ZkOptLocalDateField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<LocalDateTime>.unaryPlus(): ZkLocalDateTimeField =
        add(this) {
            ZkLocalDateTimeField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<LocalDateTime?>.unaryPlus(): ZkOptLocalDateTimeField =
        add(this) {
            ZkOptLocalDateTimeField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<Secret>.unaryPlus(): ZkSecretField =
        add(this) {
            ZkSecretField(this@ZkForm, this)
        }

    operator fun KMutableProperty0<Secret?>.unaryPlus(): ZkOptSecretField =
        add(this) {
            ZkOptSecretField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<UUID>.unaryPlus(): ZkUuidField =
        add(this) {
            ZkUuidField(this@ZkForm, it)
        }

    operator fun KMutableProperty0<UUID?>.unaryPlus(): ZkOptUuidField =
        add(this) {
            ZkOptUuidField(this@ZkForm, it)
        }

    inline operator fun <reified E : Enum<E>> KMutableProperty0<E>.unaryPlus(): ZkEnumSelectField<E> =
        add(this) { prop ->
            val options = enumValues<E>().map { it to localizedStrings.getNormalized(it.name) } // this is a non-translated to translated mapping
            add(this) {
                ZkEnumSelectField(this@ZkForm, prop) { enumValueOf(it) }.apply {
                    fetch = { options }
                }
            }
        }

    @JsName("FormOptEnumUnaryPlus")
    inline operator fun <reified E : Enum<E>> KMutableProperty0<E?>.unaryPlus(): ZkOptEnumSelectField<E> =
        add(this) { prop ->
            val options = enumValues<E>().map { it to localizedStrings.getNormalized(it.name) } // this is a non-translated to translated mapping
            add(this) {
                ZkOptEnumSelectField(this@ZkForm, prop) { enumValueOf(it) }.apply {
                    fetch = { options }
                }
            }
        }

    // -------------------------------------------------------------------------
    //  Property field convenience methods
    // ------------------------------------------------------------------------

    /**
     * Find a field for this property.
     */
    fun KMutableProperty0<*>.find(): ZkFieldBase<*> {
        return fields.first { it.propName == this.name }
    }

    // -------------------------------------------------------------------------
    //  Option setters
    // ------------------------------------------------------------------------

    // These setters are here because I want the editor to show the setter in different color.
    // I know this is a minor detail, but I feel it makes the form code much more readable.

    infix fun ZkElement?.label(value: String): ZkElement? {
        if (this is ZkFieldBase<*>) this.labelText = value
        return this
    }

    infix fun ZkElement?.readOnly(value: Boolean): ZkElement? {
        if (this is ZkFieldBase<*>) this.readOnly = value
        return this
    }

    infix fun <VT> ZkSelectBase<VT>.sort(value: Boolean): ZkSelectBase<VT> {
        sort = value
        return this
    }

    infix fun <VT> ZkSelectBase<VT>.query(block: suspend () -> List<Pair<VT, String>>): ZkSelectBase<VT> {
        fetch = block
        return this
    }

}