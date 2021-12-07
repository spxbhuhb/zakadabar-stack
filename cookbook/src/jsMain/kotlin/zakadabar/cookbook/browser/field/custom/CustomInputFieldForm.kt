/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.field.custom

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.toast.toastInfo
import zakadabar.core.resource.localized
import zakadabar.core.util.UUID
import zakadabar.core.util.default

class CustomInputFieldForm: ZkForm<ExampleBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleBo>()

    override fun onCreate() {
        super.onCreate()

        + stringField { "String" } label "string" onChange { value -> toastInfo { value } }
        + optStringField { null } label "optional string"  onChange { value -> toastInfo { value.toString() } }
        + intField { 5 } label "int"  onChange { value -> toastInfo { value.toString() } }
        + optIntField { null } label "optional int" onChange { value -> toastInfo { value?.toString() ?: "" } }
        + doubleField { 5.5 } label "double" onChange { value -> toastInfo { value.toString() } }
        + optDoubleField { null } label "optional double" onChange { value -> toastInfo { value?.toString() ?: "" } }
        + longField { 555555L } label "long" onChange { value -> toastInfo { value.toString() } }
        + optLongField { null } label "optional long" onChange { value -> toastInfo { value?.toString() ?: ""} }
        + booleanField { true } label "boolean" onChange { value -> toastInfo { value.toString() } }
        + localDateField {
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        } label "local date" onChange { value -> toastInfo { value.localized } }
        + optLocalDateField {
            null
        } label "optional local date" onChange { value -> toastInfo { value?.localized ?: ""} }
        + localDateTimeField {
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        } label "local date time" onChange { value -> toastInfo { value.localized } }
        + optLocalDateField {
            null
        } label "optional local date time" onChange { value -> toastInfo { value?.localized ?: "" } }
        + instantField { Clock.System.now() } label "instant"
        + optIntField { null } label "optional instant"
        + textAreaField { "text area" } label "text area" onChange { value -> toastInfo { value } }
        + optTextAreaField { null } label "optional text area" onChange { value -> toastInfo { value } }
        + uuidField { UUID() } label "uuid" onChange { value -> toastInfo { value.toString() } }
        + optUuidField { null } label "no uuid" onChange { value -> toastInfo { value?.toString() ?: "" } }

    }
}