/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.builtin.crud

import zakadabar.demo.data.builtin.BuiltinDto
import zakadabar.demo.data.builtin.ExampleReferenceDto
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.ZkSecretVerificationField
import zakadabar.stack.frontend.util.plusAssign

class BuiltinForm : ZkForm<BuiltinDto>() {

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
                    + select(::recordSelectValue) { ExampleReferenceDto.all().by { it.name } }
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
                    + select(::optRecordSelectValue) { ExampleReferenceDto.all().by { it.name } }
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