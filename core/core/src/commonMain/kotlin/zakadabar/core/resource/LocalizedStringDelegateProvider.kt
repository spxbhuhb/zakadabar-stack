/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

operator fun String.provideDelegate(thisRef: Nothing?, prop: KProperty<*>): ReadOnlyProperty<Nothing?, String> {
    val key = prop.name
    localizedStrings.map[key] = this
    localizedStrings.normalizedKeyMap[key.lowercase()] = this
    return LocalizedStringDelegate(key)
}

operator fun String.provideDelegate(thisRef: Any, prop: KProperty<*>): ReadOnlyProperty<Any, String> {
    val key = prop.name
    localizedStrings.map[key] = this
    localizedStrings.normalizedKeyMap[key.lowercase()] = this
    return LocalizedStringDelegate(key)
}

