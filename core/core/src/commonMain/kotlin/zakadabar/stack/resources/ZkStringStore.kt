/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.resources

import zakadabar.stack.data.builtin.misc.StringPair
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
        thisRef.normalizedKeyMap[prop.name.lowercase()] = this
        return StringsDelegate()
    }

    val childStores = mutableListOf<ZkStringStore>()

    operator fun plusAssign(childStore : ZkStringStore) {
        childStores += childStore
        childStore.map.forEach {
            map[it.key] = it.value
        }
        childStore.normalizedKeyMap.forEach {
            normalizedKeyMap[it.key] = it.value
        }
    }

    /**
     * Merge a list of locale strings into this string store. Overrides any
     * strings that are already in the store. Adds any new strings that are
     * not in the store yet.
     *
     * Also overrides child stores, but only the keys that are present in them.
     * This propagates the translations but does not multiply all strings.
     * We have to improve this on the long run.
     *
     * @return  the string store merge is called on
     */
    fun merge(other: List<StringPair>) {
        other.forEach {
            val key = it.first
            val value = it.second
            val normalizedKey = normalizeKey(key)

            map[key] = value
            normalizedKeyMap[normalizedKey] = value

            childStores.forEach { childStore ->
                if (childStore.map.containsKey(value)) {
                    childStore.map[key] = value
                    childStore.normalizedKeyMap[normalizedKey] = value
                }
            }
        }
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
     * @param   instance  The instance to look up.
     * @return  the string value that belongs to this key
     */
    fun getNormalized(instance : Any) : String {
        val key = instance::class.simpleName ?: ""
        return map[key] ?: normalizedKeyMap[normalizeKey(key)] ?: key
    }

    fun normalizeKey(key: String) = key.lowercase().replace("-", "")

}