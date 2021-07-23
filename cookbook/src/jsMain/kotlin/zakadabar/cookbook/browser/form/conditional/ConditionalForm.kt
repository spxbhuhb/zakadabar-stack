/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.form.conditional

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.util.PropertyList
import zakadabar.stack.util.default
import zakadabar.stack.util.propertyList

class ConditionalFormExample : ZkElement() {
    override fun onCreate() {
        super.onCreate()

        val bo1 = default<ExampleBo> { }

        + ConditionalForm(
            readOnly = propertyList(bo1::intValue),
            writable = propertyList(bo1::doubleValue)
        ).also { it.bo = bo1 }

        val bo2 = default<ExampleBo> { }

        + ConditionalForm(
            excluded = propertyList(bo1::doubleValue, bo2::stringValue)
        ).also { it.bo = bo2 }

        val bo3 = default<ExampleBo> { }

        + ConditionalForm()
            .also { it.bo = bo3 }
    }
}

class ConditionalForm(
    override val readOnly: PropertyList = emptyList(),
    override val writable: PropertyList = emptyList(),
    override val excluded: PropertyList = emptyList()
) : ZkForm<ExampleBo>() {

    override fun onConfigure() {
        mode = ZkElementMode.Other
    }

    override fun onCreate() {
        super.onCreate()

        + section {
            + bo::intValue
            + bo::doubleValue
            + bo::stringValue
        }
    }
}