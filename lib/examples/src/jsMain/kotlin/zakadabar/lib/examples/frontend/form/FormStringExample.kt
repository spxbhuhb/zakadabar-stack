/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.DtoSchema
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.util.default

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
) : DtoBase {
    override fun schema() = DtoSchema {
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
        dto = default { }
        mode = ZkElementMode.Action
        setAppTitle = false
    }

    override fun onCreate() {
        super.onCreate()

        + section {
            + dto::value
            + dto::optValue
            + dto::invalidValue
            + dto::readOnlyValue readOnly true
            + select(dto::selectValue, options = listOf("Option 1", "Option 2", "Option 3"))
            + textarea(dto::textAreaValue)
        }

        // Make invalidValue touched, so the form will show styles.
        // This is just for the example, not needed in actual code.

        with(dto::invalidValue.find()) {
            touched = true
            invalidInput = true
        }

        validate()
    }

}