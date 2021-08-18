/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LocalizedStringDelegate<T>(
    val key : String
) : ReadOnlyProperty<T,String> {

    override fun getValue(thisRef: T, property: KProperty<*>): String {
        return localizedStrings[key]
    }

}
