/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.field

import zakadabar.core.browser.field.select.DropdownRenderer
import zakadabar.core.browser.field.select.SelectRenderer
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import kotlin.reflect.KMutableProperty0

open class ZkPropEntitySelectField<ST : EntityBo<ST>>(
    context : ZkFieldContext,
    val prop: KMutableProperty0<EntityId<ST>>,
    renderer : SelectRenderer<EntityId<ST>,ZkPropEntitySelectField<ST>> = DropdownRenderer()
) : ZkSelectBaseV2<EntityId<ST>,ZkPropEntitySelectField<ST>>(
    context = context,
    label = prop.name,
    renderer = renderer,
    // prop.get() returns with undefined, without the check the code fails, have to check why
    // TODO check prop entity select null prop value
    getter = { prop.get()?.let { if (it.isEmpty()) null else it } }
) {

    override fun fromString(string: String): EntityId<ST> {
        return EntityId(string)
    }

    override fun setBackingValue(value: Pair<EntityId<ST>, String>?, user : Boolean) {
        if (value == null) {
            invalidInput = true
            if (user) context.validate()
        } else {
            invalidInput = false
            prop.set(value.first)
            if (user) onUserChange(value.first)
        }
    }

}