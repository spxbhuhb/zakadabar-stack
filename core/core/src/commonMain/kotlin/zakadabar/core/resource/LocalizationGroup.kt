/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

open class LocalizationGroup(
    val name: String
) {

    constructor(klass : KClass<*>) : this(klass.simpleName!!)

    open operator fun String.provideDelegate(thisRef: LocalizationGroup, prop: KProperty<*>): ReadOnlyProperty<LocalizationGroup, String> {
        val key = name + "." + prop.name
        localizedStrings.map[key] = this
        localizedStrings.normalizedKeyMap[key.lowercase()] = this
        return LocalizedStringDelegate(key)
    }

}
