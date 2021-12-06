/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.crud.basic

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.cookbook.resource.strings
import zakadabar.core.browser.field.ZkPropStringField
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.resource.localized
import zakadabar.core.resource.localizedStrings

class BasicInlineCrudForm : ZkForm<ExampleBo>() {

    val options = listOf("option 1", "option 2", "option3").map { it to it }

    override fun onCreate() {
        super.onCreate()

        build(localized<BasicInlineCrudForm>()) {
            + section {
                + bo::id
                + bo::booleanValue
                + bo::doubleValue
                + bo::enumSelectValue
                + bo::instantValue
                + bo::intValue
                + bo::localDateValue
                + bo::localDateTimeValue
                + opt(bo::optBooleanValue, localizedStrings.trueText, localizedStrings.falseText)
                + bo::optDoubleValue
                + bo::optEnumSelectValue
                + bo::optInstantValue
                + bo::optIntValue
                + bo::optLocalDateValue
                + bo::optLocalDateTimeValue readOnly true
                + bo::optSecretValue
                + bo::optRecordSelectValue query { ExampleReferenceBo.all().by { it.name } }
                + bo::optStringValue
                + bo::optStringSelectValue.asSelect() query { options }
                + bo::optTextAreaValue
                + bo::optUuidValue
                + bo::secretValue
                + bo::recordSelectValue query { ExampleReferenceBo.all().by { it.name } }
                + bo::recordSelectValue query { ExampleReferenceBo.all().by { it.name } } readOnly true
                + ZkPropStringField(this@BasicInlineCrudForm, bo::stringValue).also { this@BasicInlineCrudForm.fields += it }
                + bo::stringSelectValue.asSelect() query { options }
                + bo::textAreaValue.asTextArea() label strings.textAreaValue
                + bo::uuidValue
            }
        }
    }
}