/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.resources

import zakadabar.stack.data.builtin.resources.TranslationDto
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Stores constant strings meant to display for the user. On the frontend
 * this is labels, messages etc.
 */
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

    /**
     * Merge a list of locale strings into this string store. Overrides any
     * strings that are already in the store. Adds any new strings that are
     * not in the store yet.
     *
     * @return  the string store merge is called on
     */
    inline fun <reified T> merge(other: List<TranslationDto>): T {
        other.forEach {
            map[it.name] = it.value
        }
        return this as T
    }

    /**
     * Get a value for the given key.
     */
    operator fun get(key : String) = map[key] ?: key

}