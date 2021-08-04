/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import kotlin.reflect.KProperty

typealias PropertyList = List<String>

fun propertyList(vararg properties : KProperty<*>) : PropertyList = properties.map { it.name }