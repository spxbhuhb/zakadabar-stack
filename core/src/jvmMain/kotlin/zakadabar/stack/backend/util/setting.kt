/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.util

import zakadabar.stack.data.DtoBase
import kotlin.reflect.KProperty

class Setting<V : DtoBase>(
    var value: V,
    var namespace: String
) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        this.value = value
    }

    init {

    }
}