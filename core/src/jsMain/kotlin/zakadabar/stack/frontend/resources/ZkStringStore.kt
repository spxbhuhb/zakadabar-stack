/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources

import zakadabar.stack.frontend.application.ZkApplication
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class ZkStringStore(
    val map: MutableMap<String, String> = mutableMapOf()
) {

    companion object {
        /**
         * Translates the given string if there is a translation in the application's
         * string store. Returns with the original string when there is no translation.
         */
        fun t(original: String) = ZkApplication.stringStore.map[original] ?: original
    }

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

