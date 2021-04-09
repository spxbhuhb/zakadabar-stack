/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.lib.form

import zakadabar.demo.data.builtin.BuiltinDto
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.ZkSecretVerificationField
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles.grow
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.plusAssign

/**
 * This example shows all built in form fields.
 */
object FormFields : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        // this makes the form to fill the whole page
        classList += grow

        val form = Form()
        form.dto = default()
        form.mode = ZkElementMode.Action
        + form
    }

    class Form : ZkForm<BuiltinDto>() {

        override fun onCreate() {
            build(Strings.formFields) {

                buildElement.classList += ZkFormStyles.twoPanels

                + section(Strings.mandatoryFields) {
                    with(dto) {
                        + ::id
                        + ::booleanValue
                        + ::doubleValue
                        + ::enumSelectValue
                        + ::intValue
                        + ::instantValue
                        + ::secretValue
                        + ZkSecretVerificationField(this@Form, ::secretValue)
                        + select(::recordSelectValue) { emptyList() }
                        + ::stringValue
                        + select(::stringSelectValue, options = listOf("option1", "option2", "option3"))
                        + textarea(::textAreaValue)
                        + ::uuidValue
                    }
                }

                + section(Strings.optionalFields) {
                    with(dto) {
                        + opt(::optBooleanValue, "true", "false")
                        + ::optDoubleValue
                        + ::optEnumSelectValue
                        + ::optInstantValue
                        + ::optIntValue
                        + ::optSecretValue
                        + select(::optRecordSelectValue) { emptyList() }
                        + ::optStringValue
                        + select(::optStringSelectValue, options = listOf("option1", "option2", "option3"))
                        + textarea(::optTextAreaValue)
                        + ::optUuidValue
                    }
                }

                + ZkButton(Strings.validate) {
                    validate(true)
                }

            }
        }
    }

}