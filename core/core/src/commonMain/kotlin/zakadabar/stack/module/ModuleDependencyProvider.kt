/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.module

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ModuleDependencyProvider<T : Any>(
    private val moduleClass: KClass<T>,
    private val selector: (T) -> Boolean
) {
    operator fun provideDelegate(thisRef: Any, property: KProperty<*>) =
        ModuleDependency(thisRef, property, moduleClass, selector)

    operator fun provideDelegate(thisRef: Nothing?, property: KProperty<*>) =
        ModuleDependency(thisRef, property, moduleClass, selector)
}
