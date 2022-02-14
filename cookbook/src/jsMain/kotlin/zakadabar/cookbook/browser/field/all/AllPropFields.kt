/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.field.all

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.cookbook.resource.strings
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.field.ZkPropStringField
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.resource.localizedStrings
import zakadabar.core.util.default

class AllPropFields : ZkForm<ExampleBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleBo>()

    val options = listOf("option 1", "option 2", "option3").map { it to it }

    override fun onCreate() {
        super.onCreate()

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
            + ZkPropStringField(this@AllPropFields, bo::stringValue).also { this@AllPropFields.fields += it }
            + bo::stringSelectValue.asSelect() query { options }
            + bo::textAreaValue.asTextArea() label strings.textAreaValue
            + bo::uuidValue
        }

        + fieldTable()
    }

}