/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.builtin.form

import zakadabar.demo.data.builtin.BuiltinDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.ZkSecretVerificationField
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles.grow
import zakadabar.stack.frontend.builtin.pages.ZkElementMode
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

                style {
                    display = "grid"
                }

                buildElement.style.setProperty("grid-template-columns", "1fr 1fr")
                buildElement.style.setProperty("gap", "${theme.layout.marginStep * 2}px")


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
                    }
                }

                + section(Strings.optionalFields) {
                    with(dto) {
                        + opt(::optBooleanValue, "true", "false")
                        + ::optEnumSelectValue
                        + ::optInstantValue
                        + ::optSecretValue
                        + select(::optRecordSelectValue) { emptyList() }
                        + ::optStringValue
                        + select(::optStringSelectValue, options = listOf("option1", "option2", "option3"))
                        + textarea(::optTextAreaValue)
                    }
                }

                + ZkButton(Strings.validate) {
                    validate(true)
                }

            }
        }
    }

}