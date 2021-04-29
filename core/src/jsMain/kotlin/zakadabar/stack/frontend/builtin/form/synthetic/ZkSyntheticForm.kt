/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.synthetic

import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.dto.DescriptorDto
import zakadabar.stack.data.schema.dto.IntPropertyDto
import zakadabar.stack.data.schema.dto.StringPropertyDto
import zakadabar.stack.frontend.application.ZkApplication.t
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.ZkIntField
import zakadabar.stack.frontend.builtin.form.fields.ZkStringField
import zakadabar.stack.frontend.util.plusAssign

class ZkSyntheticForm(
    private val descriptor: DescriptorDto
) : ZkForm<DtoBase>() {

    override fun onCreate() {
        super.onCreate()

        buildElement.classList += ZkFormStyles.onePanel

        + section {
            descriptor.properties.forEach { dProperty ->

                val field = when (dProperty) {
                    is IntPropertyDto -> ZkIntField(this@ZkSyntheticForm, dProperty::value)
                    is StringPropertyDto -> ZkStringField(this@ZkSyntheticForm, dProperty::value)
                    else -> null
                } ?: return@forEach

                field.label = t(dProperty.name)
                fields += field
                + field

            }
        }

    }

}