/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.synthetic

import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.descriptor.BoDescriptor
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.util.plusAssign

class ZkSyntheticForm(
    private val boDescriptor: BoDescriptor
) : ZkForm<DtoBase>() {

    override fun onCreate() {
        super.onCreate()

        buildPoint.classList += ZkFormStyles.onePanel

        + section {
            boDescriptor.properties.forEach { dProperty ->

//                val field = when (dProperty) {
//                    is IntPropertyDto -> ZkIntField(this@ZkSyntheticForm, dProperty::value)
//                    is StringPropertyDto -> ZkStringField(this@ZkSyntheticForm, dProperty::value)
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