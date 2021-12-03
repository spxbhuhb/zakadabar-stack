/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.field.onchange

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.field.*
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.data.EntityId
import zakadabar.core.util.default

open class FieldOnChangeForm : ZkForm<ExampleBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleBo>()

    lateinit var recordSelectField: ZkEntitySelectField<ExampleReferenceBo>

    val options = listOf("option 1", "option 2", "option3").map { it to it }

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.inlineForm

        + section {
            + bo::booleanValue onChange ::onBooleanChange

            + bo::doubleValue onChange { toastSuccess { "doubleValue = $it" } }

            + bo::intValue onChange3 ::onIntValueChange

            + bo::stringValue onChange3 { _, value, _ -> toastSuccess { "stingValue = $value" } }

            + bo::recordSelectValue query ::queryRecords onChange3 ::onRecordSelectChange saveAs ::recordSelectField

            + bo::stringSelectValue.asSelect() query { options } onChange3 ::onStringSelectChange
        }

        + buttonPrimary("Change intValue to 15") {
            (bo::intValue.find() as ZkIntPropField).value = 15
        }
    }

    open suspend fun queryRecords(): List<Pair<EntityId<ExampleReferenceBo>, String>> =
        ExampleReferenceBo.all().by { it.name }

    open fun onRecordSelectChange(
        origin: ChangeOrigin,
        value: EntityId<ExampleReferenceBo>,
        field: ZkEntitySelectField<ExampleReferenceBo>
    ) {
        toastSuccess { "recordSelectValue = $value, field.selectedItem (OLD VALUE) = ${field.selectedItem}" }
    }

    open fun onBooleanChange(value: Boolean) {
        toastSuccess { "booleanValue = $value" }
    }

    open fun onIntValueChange(origin: ChangeOrigin, value: Int, field: ZkFieldBase<Int, ZkIntPropField>) {
        toastSuccess { "intValue = $value, origin = $origin" }
    }

    open fun onStringSelectChange(
        origin: ChangeOrigin,
        value: String,
        field: ZkStringSelectField
    ) {
        toastSuccess { "stringSelectValue = $value, field.selectedItem (OLD VALUE) = ${field.selectedItem} " }
    }
}