/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.module

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ModuleDependency<T : Any>(
    dependentModule: Any?,
    private val dependentProperty: KProperty<*>,
    private val moduleClass: KClass<T>,
    private val selector: (T) -> Boolean
) {
    private var module: T? = modules.firstOrNull(moduleClass, selector)

    init {
        dependencies += this
    }

    val name = dependentModule?.let { moduleName(it::class) + "." } ?: ""

    // TODO think about synchronization in dependency resolution
    fun resolve() =
        try {
            module = modules.first(moduleClass, selector)
            moduleLogger.info("resolved dependency from ${name}${dependentProperty.name} to ${moduleClass.simpleName} ")
            true
        } catch (ex: NoSuchElementException) {
            moduleLogger.error("unable to resolve dependency from ${name}${dependentProperty.name} to ${moduleClass.simpleName} ")
            false
        }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return module !!
    }

}