/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.form.select.radio

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleEnum
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.toast.ZkToast
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.css.px
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.default

class RadioBo(
    var enumSelectValue: ExampleEnum,
    var optEnumSelectValue: ExampleEnum?,
    var optStringSelectValue: String?,
    var stringSelectValue: String,
) : BaseBo {
    override fun schema() = BoSchema {
        + ::enumSelectValue
        + ::optEnumSelectValue
        + ::optStringSelectValue max 100
        + ::stringSelectValue blank false max 50
    }
}

class RadioGroupSelect : ZkForm<RadioBo>() {

    override var mode = ZkElementMode.Other

    init {
        bo = default {
            enumSelectValue = ExampleEnum.EnumValue3
        }
    }

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.inlineForm

        val options = listOf("option 1", "option 2", "option3").map { it to it }

        + grid {
            gridGap = 10.px
            gridTemplateColumns = "1fr 1fr"

            + div { + bo::enumSelectValue.asRadioGroup() }

            + div { + bo::optEnumSelectValue.asRadioGroup() readOnly true }

            + div { + bo::stringSelectValue.asRadioGroup() query { options } }

            + div { + bo::optStringSelectValue.asRadioGroup() query { options } }
        }

        + buttonPrimary("Validate") {
            validate(true)
        }

    }

    override fun validate(submit: Boolean): Boolean {
        ZkToast("${bo.enumSelectValue}   ${bo.optEnumSelectValue}   ${bo.stringSelectValue}   ${bo.optStringSelectValue}").run()
        return super.validate(submit)
    }
}