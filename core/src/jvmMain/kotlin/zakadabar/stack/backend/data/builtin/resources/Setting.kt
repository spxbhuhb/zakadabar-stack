/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.resources

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.DtoBase
import kotlin.reflect.KProperty

class Setting<V : DtoBase>(
    var value: V,
    val namespace: String,
    private val serializer: KSerializer<V>
) {

    private var initialized: Boolean = false

    operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
        synchronized(this) {
            if (! initialized) {
                value = SettingBackend.get(value, namespace, serializer)
                initialized = true
            }
        }
        return value
    }

}