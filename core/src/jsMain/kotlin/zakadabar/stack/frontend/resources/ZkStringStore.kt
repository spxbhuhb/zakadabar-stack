/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class ZkStringStore(
    val map: MutableMap<String, String> = mutableMapOf()
) {
    class StringsDelegate : ReadOnlyProperty<ZkStringStore, String> {
        override fun getValue(thisRef: ZkStringStore, property: KProperty<*>): String {
            return thisRef.map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: ZkStringStore, prop: KProperty<*>): ReadOnlyProperty<ZkStringStore, String> {
        thisRef.map[prop.name] = this
        return StringsDelegate()
    }

}

