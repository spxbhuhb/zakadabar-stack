/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.form

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.resources.strings
import zakadabar.core.data.BaseBo
import zakadabar.core.data.schema.BoSchema
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.util.default

/**
 * DTO classes are usually defined in commonMain. This one here is to make the
 * example easier to write, but it is not accessible on the backend and it has
 * no communication feature.
 */
class BooleanExampleDto(
    var value: Boolean,
    var optValue: Boolean?,
    var readOnlyValue: Boolean
) : BaseBo {
    override fun schema() = BoSchema {
        + ::value
        + ::optValue
        + ::readOnlyValue
    }
}

/**
 * This example shows boolean form fields.
 */
class FormBooleanExample(
    element: HTMLElement
) : ZkForm<BooleanExampleDto>(element) {

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
            + opt(bo::optValue, strings.textForFalse, strings.textForTrue)
            + bo::readOnlyValue readOnly true
        }
    }

}