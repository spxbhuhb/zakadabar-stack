/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.lib.examples.data.builtin.ExampleReferenceDto
import zakadabar.lib.examples.resources.Strings
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.util.plusAssign

class BuiltinForm : ZkForm<BuiltinDto>() {

    override fun onCreate() {
        super.onCreate()

        build(Strings.formFields) {

            buildPoint.classList += ZkFormStyles.twoPanels

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