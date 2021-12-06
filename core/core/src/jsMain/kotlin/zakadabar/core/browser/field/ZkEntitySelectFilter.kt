/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import zakadabar.core.browser.field.select.DropdownRenderer
import zakadabar.core.browser.field.select.SelectRenderer
import zakadabar.core.data.EntityId

//open class ZkEntitySelectFilter(
//    context: ZkFieldContext,
//    var getValue: () -> EntityId<*>?,
//    renderer : SelectRenderer<EntityId<*>,ZkEntitySelectFilter> = DropdownRenderer(),
//    onSelected: (Pair<EntityId<*>, String>?) -> Unit
//) : ZkSelectBaseV2<EntityId<*>,ZkEntitySelectFilter>(context, "", renderer, onSelected) {
//
//    override fun fromString(string: String): EntityId<*> {
//        return items.first().first
//    }
//
//    override fun getPropValue() = getValue()
//
//    override fun setPropValue(value: Pair<EntityId<*>, String>?, user : Boolean) {
//        // do nothing here, onSelected will be called by ZkSelectBase
//    }
//
//}
