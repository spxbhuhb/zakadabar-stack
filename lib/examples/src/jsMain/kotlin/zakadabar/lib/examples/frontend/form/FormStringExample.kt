/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.util.default
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema

/**
 * DTO classes are usually defined in commonMain. This one here is to make the
 * example easier to write, but it is not accessible on the backend and it has
 * no communication feature.
 */
class StringExampleDto(
    var value: String,
    var optValue: String?,
    var invalidValue: String,
    var readOnlyValue: String,
    var selectValue : String,
    var textAreaValue : String,
) : BaseBo {
    override fun schema() = BoSchema {
        + ::value
        + ::optValue
        + ::invalidValue default "invalid content"
        + ::readOnlyValue default "read only content"
        + ::selectValue default "Option 1"
        + ::textAreaValue default "content of the text area"
    }
}

/**
 * This example shows boolean form fields.
 */
class FormStringExample(
    element: HTMLElement
) : ZkForm<StringExampleDto>(element) {

    override fun onConfigure() {
        super.onConfigure()
        bo = default { }
        mode = ZkElementMode.Action
        setAppTitle = false
    }

    override fun onCreate() {
        super.onCreate()

        + section {
            + bo::value
            + bo::optValue
            + bo::invalidValue
            + bo::readOnlyValue readOnly true
            + bo::selectValue.asSelect() options { listOf("Option 1", "Option 2", "Option 3") }
            + bo::textAreaValue.asTextArea()
        }

        // Make invalidValue touched, so the form will show styles.
        // This is just for the example, not needed in actual code.

        with(bo::invalidValue.find()) {
            touched = true
            invalidInput = true
        }

        validate()
    }

}