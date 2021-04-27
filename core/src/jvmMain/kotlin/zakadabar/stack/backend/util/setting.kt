/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import zakadabar.stack.backend.Server
import zakadabar.stack.data.DtoBase
import kotlin.reflect.KProperty
import kotlin.reflect.full.createType

@Suppress("UNCHECKED_CAST") // serializer should create KSerializer<T> for sure
inline fun <reified T : DtoBase> setting(namespace: String) =
    Setting(default(), namespace, serializer(T::class.createType()) as KSerializer<T>)


class Setting<V : DtoBase>(
    var value: V,
    val namespace: String,
    serializer: KSerializer<V>
) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        this.value = value
    }

    init {
        value = Server.loadSettings(value, serializer, namespace)
        Server.overrideSettings(value, namespace)
    }
}