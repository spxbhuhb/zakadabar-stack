/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.field.update

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.field.ChangeOrigin
import zakadabar.core.browser.field.ZkEntitySelectField
import zakadabar.core.browser.field.ZkFieldBase
import zakadabar.core.browser.field.ZkPropIntField
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.data.EntityId
import zakadabar.core.resource.css.px
import zakadabar.core.util.default

open class FieldUpdateForm : ZkForm<ExampleBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleBo>()

    lateinit var recordSelectField: ZkEntitySelectField<ExampleReferenceBo>

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.inlineForm

        + section {
            + bo::intValue onChange3 ::onIntValueChange

            + bo::recordSelectValue query ::queryRecords onChange3 ::onRecordSelectChange saveAs ::recordSelectField
        }

        + row {
            + buttonPrimary("Change intValue to 15") {
                (bo::intValue.find() as ZkPropIntField).value = 15
            } marginRight 20.px

            + buttonPrimary("Change recordSelectValue to first option") {
                recordSelectField.value = recordSelectField.items.first().first
            }
        }
    }

    open suspend fun queryRecords(): List<Pair<EntityId<ExampleReferenceBo>, String>> =
        ExampleReferenceBo.all().by { it.name }

    open fun onRecordSelectChange(
        origin: ChangeOrigin,
        value: EntityId<ExampleReferenceBo>,
        field: ZkEntitySelectField<ExampleReferenceBo>
    ) {
        toastSuccess { "recordSelectValue = $value, origin = $origin" }
    }

    open fun onIntValueChange(origin: ChangeOrigin, value: Int, field: ZkFieldBase<Int, ZkPropIntField>) {
        toastSuccess { "intValue = $value, origin = $origin" }
    }

}