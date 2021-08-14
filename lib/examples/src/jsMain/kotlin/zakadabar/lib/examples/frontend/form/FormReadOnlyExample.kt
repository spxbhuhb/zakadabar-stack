/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.data.builtin.ExampleReferenceBo
import zakadabar.lib.examples.resources.strings
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.form.fields.ZkStringField
import zakadabar.core.frontend.builtin.layout.zkLayoutStyles
import zakadabar.core.frontend.builtin.toast.toastSuccess
import zakadabar.core.frontend.util.default
import zakadabar.core.resource.localized
import zakadabar.core.resource.localizedStrings

class FormReadOnlyExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        + div {
            + zkLayoutStyles.p1
            + zkLayoutStyles.fixBorder

            + ReadOnlyBuiltinForm().apply {
                bo = default { }
                mode = ZkElementMode.Other
                setAppTitle = false
                onBack = { toastSuccess { "You've just clicked on \"Back\"." } }
            }
        }
    }

}

class ReadOnlyBuiltinForm : ZkForm<BuiltinBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<ReadOnlyBuiltinForm>()) {
            + section {
                + bo::id readOnly true
                + bo::booleanValue readOnly true
                + bo::doubleValue readOnly true
                + bo::enumSelectValue readOnly true
                + bo::intValue readOnly true
                + bo::instantValue readOnly true
                + opt(bo::optBooleanValue, localizedStrings.trueText, localizedStrings.falseText) readOnly true
                + bo::optDoubleValue readOnly true
                + bo::optEnumSelectValue readOnly true
                + bo::optInstantValue readOnly true
                + bo::optIntValue readOnly true
                + bo::optSecretValue readOnly true
                + bo::optRecordSelectValue query { ExampleReferenceBo.all().by { it.name } } readOnly true
                + bo::optStringValue readOnly true
                + select(bo::optStringSelectValue, options = listOf("option 1", "option 2", "option3")) readOnly true
                + bo::optTextAreaValue readOnly true
                + bo::optUuidValue readOnly true
                + bo::secretValue readOnly true
                + bo::recordSelectValue query { ExampleReferenceBo.all().by { it.name } } readOnly true
                + ZkStringField(this@ReadOnlyBuiltinForm, bo::stringValue).also { this@ReadOnlyBuiltinForm.fields += it } readOnly true
                + select(bo::stringSelectValue, options = listOf("option 1", "option 2", "option3")) readOnly true
                + textarea(bo::textAreaValue) label strings.textAreaValue readOnly true
                + bo::uuidValue readOnly true
            }
        }
    }
}