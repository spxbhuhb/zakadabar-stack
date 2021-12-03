/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.field.ZkStringPropField
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.browser.util.default
import zakadabar.core.resource.localized
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.data.builtin.ExampleReferenceBo
import zakadabar.lib.examples.resources.strings

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
                + opt(bo::optBooleanValue, localizedStrings.trueText, localizedStrings.falseText).also { it.readOnly = true }
                + bo::optDoubleValue readOnly true
                + bo::optEnumSelectValue readOnly true
                + bo::optInstantValue readOnly true
                + bo::optIntValue readOnly true
                + bo::optSecretValue readOnly true
                + bo::optRecordSelectValue query { ExampleReferenceBo.all().by { it.name } } readOnly true
                + bo::optStringValue readOnly true
                + bo::optStringSelectValue.asSelect() query { listOf("option 1", "option 2", "option3").map { it to it } } readOnly true
                + bo::optTextAreaValue readOnly true
                + bo::optUuidValue readOnly true
                + bo::secretValue readOnly true
                + bo::recordSelectValue query { ExampleReferenceBo.all().by { it.name } } readOnly true
                + ZkStringPropField(this@ReadOnlyBuiltinForm, bo::stringValue).also { this@ReadOnlyBuiltinForm.fields += it } .also { it.readOnly = true }
                + bo::stringSelectValue.asSelect() query { listOf("option 1", "option 2", "option3").map { it to it } } readOnly true
                + bo::textAreaValue.asTextArea() label strings.textAreaValue readOnly true
                + bo::uuidValue readOnly true
            }
        }
    }
}