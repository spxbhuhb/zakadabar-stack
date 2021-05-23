/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.resources

import zakadabar.stack.data.builtin.resources.TranslationBo
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Stores constant strings meant to display for the user. On the frontend
 * this is labels, messages etc.
 *
 * @property  map               Values in this string store.
 * @property  normalizedKeyMap  Same values as in [map] but the keys are normalized.
 *                              Normalization means: lowercase, all hyphens removed.
 */
open class ZkStringStore(
    val map: MutableMap<String, String> = mutableMapOf(),
    val normalizedKeyMap: MutableMap<String, String> = mutableMapOf()
) {

    class StringsDelegate : ReadOnlyProperty<ZkStringStore, String> {
        override fun getValue(thisRef: ZkStringStore, property: KProperty<*>): String {
            return thisRef.map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: ZkStringStore, prop: KProperty<*>): ReadOnlyProperty<ZkStringStore, String> {
        thisRef.map[prop.name] = this
        thisRef.normalizedKeyMap[prop.name.toLowerCase()] = this
        return StringsDelegate()
    }

    /**
     * Merge a list of locale strings into this string store. Overrides any
     * strings that are already in the store. Adds any new strings that are
     * not in the store yet.
     *
     * @return  the string store merge is called on
     */
    inline fun <reified T> merge(other: List<TranslationBo>): T {
        other.forEach {
            map[it.name] = it.value
            normalizedKeyMap[normalizeKey(it.name)] = it.value
        }
        return this as T
    }

    /**
     * Get a value for the given key.
     *
     * @param   key  The key to look up.
     * @return  the string value that belongs to this key
     */
    operator fun get(key: String) = map[key] ?: key

    /**
     * Get a value for the given key with a normalized look up. This is used
     * for automatic labeling / naming.
     *
     * 1. try to get with the exact key, if it exists, return with it.
     * 1. try to get the normalized key from the [normalizedKeyMap].
     * 1. return with the key itself
     *
     * @param   key  The key to look up.
     * @return  the string value that belongs to this key
     */
    fun getNormalized(key: String) = map[key] ?: normalizedKeyMap[normalizeKey(key)] ?: key

    /**
     * Get a value for the class name of the object with a normalized look up. This is used
     * for automatic labeling / naming.
     *
     * 1. try to get with the exact key, if it exists, return with it.
     * 1. try to get the normalized key from the [normalizedKeyMap].
     * 1. return with the key itself
     *
     * @param   key  The key to look up.
     * @return  the string value that belongs to this key
     */
    fun getNormalized(any : Any) : String {
        val key = any::class.simpleName ?: ""
        return map[key] ?: normalizedKeyMap[normalizeKey(key)] ?: key
    }

    fun normalizeKey(key: String) = key.toLowerCase().replace("-", "")

}