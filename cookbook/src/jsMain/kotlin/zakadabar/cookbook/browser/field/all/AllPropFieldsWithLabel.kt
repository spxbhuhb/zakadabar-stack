/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.field.all

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.cookbook.resource.strings
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.resource.localizedStrings
import zakadabar.core.util.default

class AllPropFieldsWithLabel : ZkForm<ExampleBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleBo>()

    val options = listOf("option 1", "option 2", "option3").map { it to it }

    override fun onCreate() {
        super.onCreate()

        + section {
            + bo::id label "ID 2"
            + bo::booleanValue label "Boolean 2"
            + bo::doubleValue label "Double 2"
            + bo::enumSelectValue label "Enum Select 2"
            + bo::instantValue label "Instant 2"
            + bo::intValue label "Int 2"
            + bo::localDateValue label "Local Date 2"
            + bo::localDateTimeValue label "Local Date Time 2"
            + opt(bo::optBooleanValue, localizedStrings.trueText, localizedStrings.falseText) label "Opt Boolean 2"
            + bo::optDoubleValue label "Opt Double 2"
            + bo::optEnumSelectValue label "Opt Enum Select 2"
            + bo::optInstantValue label "Opt Instant 2"
            + bo::optIntValue label "Opt Int 2"
            + bo::optLocalDateValue label "Opt Local Date 2"
            + bo::optLocalDateTimeValue readOnly true label "Opt Local Date Time 2"
            + bo::optSecretValue label "Opt Secret 2"
            + bo::optRecordSelectValue query { ExampleReferenceBo.all().by { it.name } } label "Opt Record Select 2"
            + bo::optStringValue label "Opt String 2"
            + bo::optStringSelectValue.asSelect() query { options } label "Opt String Select 2"
            + bo::optTextAreaValue label "Opt Text Area 2"
            + bo::optUuidValue label "Opt UUID 2"
            + bo::secretValue label "Secret 2"
            + bo::recordSelectValue query { ExampleReferenceBo.all().by { it.name } } label "Record Select 2"
            + bo::stringSelectValue.asSelect() query { options } label "String Select 2"
            + bo::textAreaValue.asTextArea() label strings.textAreaValue label "Text Area 2"
            + bo::uuidValue label "UUID 2"
        }

        + fieldTable()
    }

}