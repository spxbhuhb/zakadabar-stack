/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.form.synthetic

import zakadabar.core.data.BaseBo
import zakadabar.core.data.schema.descriptor.BoDescriptor
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.form.ZkFormStyles
import zakadabar.core.frontend.util.plusAssign

class ZkSyntheticForm(
    private val boDescriptor: BoDescriptor
) : ZkForm<BaseBo>() {

    override fun onCreate() {
        super.onCreate()

        buildPoint.classList += ZkFormStyles.onePanel

        + section {
            boDescriptor.properties.forEach { _ ->

//                val field = when (dProperty) {
//                    is IntPropertyBo -> ZkIntField(this@ZkSyntheticForm, dProperty::value)
//                    is StringPropertyBo -> ZkStringField(this@ZkSyntheticForm, dProperty::value)
//                    else -> null
//                } ?: return@forEach
//
//                field.label = stringStore.getNormalized(dProperty.name)
//                fields += field
//                + field

            }
        }

    }

}