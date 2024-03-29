/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.field

import zakadabar.core.browser.field.select.DropdownRenderer
import zakadabar.core.browser.field.select.SelectRenderer
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import kotlin.reflect.KMutableProperty0

open class ZkPropOptEntitySelectField<ST : EntityBo<ST>>(
    context : ZkFieldContext,
    val prop: KMutableProperty0<EntityId<ST>?>,
    renderer : SelectRenderer<EntityId<ST>?,ZkPropOptEntitySelectField<ST>> = DropdownRenderer()
) : ZkSelectBaseV2<EntityId<ST>?,ZkPropOptEntitySelectField<ST>>(
    context = context,
    label = prop.name,
    renderer = renderer,
    getter = { prop.get() }
) {

    override fun fromString(string: String): EntityId<ST> {
        return EntityId(string)
    }

    override fun setBackingValue(value: Pair<EntityId<ST>?, String>?, user : Boolean) {
        prop.set(value?.first)
        if (user) onUserChange(value?.first)
    }

}