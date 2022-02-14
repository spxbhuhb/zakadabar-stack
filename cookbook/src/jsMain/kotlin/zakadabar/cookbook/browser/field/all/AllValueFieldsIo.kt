/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.field.all

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.util.io
import zakadabar.core.util.default

class AllValueFieldsIo : ZkForm<ExampleBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleBo>()

    val options = listOf("option 1", "option 2", "option3").map { it to it }

    override fun onCreate() {
        super.onCreate()

        io {
            + section {
                + booleanField { bo.booleanValue } label "Boolean"
                + doubleField { bo.doubleValue } label "Double"
                + enumSelectField { bo.enumSelectValue } label "Enum Select"
                + instantField { bo.instantValue } label "Instant"
                + intField { bo.intValue } label "Int"
                + localDateField { bo.localDateValue } label "Local Date"
                + localDateTimeField { bo.localDateTimeValue } label "Local Date Time"
                + optBooleanSelectField { bo.optBooleanValue } label "Opt Boolean"
                + optDoubleField { bo.optDoubleValue } label "Opt Double"
                + optEnumSelectField { bo.optEnumSelectValue } label "Opt Enum Select"
                + optInstantField { bo.optInstantValue } label "Opt Instant"
                + optIntField { bo.optIntValue } label "Opt Int"
                + optLocalDateField { bo.optLocalDateValue } label "Opt Local Date"
                + optLocalDateTimeField { bo.optLocalDateTimeValue } readOnly true label "Opt Local Date Time"
                + optStringField { bo.optStringValue } label "Opt String"
                + optStringSelectField { bo.optStringSelectValue } query { options } label "Opt String Select"
                + optTextAreaField { bo.optTextAreaValue } label "Opt Text Area"
                + optUuidField { bo.optUuidValue } label "Opt UUID"
                + stringField { bo.stringValue } label "String"
                + stringSelectField { bo.stringSelectValue } query { options } label "String Select"
                + textAreaField { bo.textAreaValue } label "Text Area"
                + uuidField { bo.uuidValue } label "UUID"
            }

            + fieldTable()
        }
    }

}