/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.module

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

var List<ModuleDependency<*>>.optional: Boolean
    get() = throw NotImplementedError()
    set(value) {
        forEach { it.optional = value }
    }

open class ModuleDependencyProvider<T : Any>(
    val moduleClass: KClass<T>,
    val selector: (T) -> Boolean
) {
    operator fun provideDelegate(thisRef: Any, property: KProperty<*>) =
        MandatoryModuleDependency(thisRef, property, moduleClass, selector)
            .also { modules += it }

    operator fun provideDelegate(thisRef: Nothing?, property: KProperty<*>) =
        MandatoryModuleDependency(thisRef, property, moduleClass, selector)
            .also { modules += it }
}

open class OptionalModuleDependencyProvider<T : Any>(
    val moduleClass: KClass<T>,
    val selector: (T) -> Boolean
) {
    operator fun provideDelegate(thisRef: Any, property: KProperty<*>) =
        OptionalModuleDependency(thisRef, property, moduleClass, selector)
            .also { modules += it }

    operator fun provideDelegate(thisRef: Nothing?, property: KProperty<*>) =
        OptionalModuleDependency(thisRef, property, moduleClass, selector)
            .also { modules += it }
}

class MandatoryModuleDependency<T : Any>(
    override val consumerModule: Any?,
    override val consumerProperty: KProperty<*>,
    override val providerClass: KClass<T>,
    override val selector: (T) -> Boolean,
    override var optional: Boolean = false
) : ModuleDependency<T> {

    override var module: T? = modules.firstOrNull(providerClass, selector)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return module !!
    }
}

class OptionalModuleDependency<T : Any>(
    override val consumerModule: Any?,
    override val consumerProperty: KProperty<*>,
    override val providerClass: KClass<T>,
    override val selector: (T) -> Boolean,
    override var optional: Boolean = true
) : ModuleDependency<T> {

    override var module: T? = modules.firstOrNull(providerClass, selector)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return module
    }

}

interface ModuleDependency<T : Any> {

    val consumerModule: Any?
    val consumerProperty: KProperty<*>
    val providerClass: KClass<T>
    val selector: (T) -> Boolean
    var optional: Boolean
    var module: T?

    val name get() = consumerModule?.let { moduleName(it::class) + "." } ?: ""

    fun resolve(): Boolean {

        module = modules.firstOrNull(providerClass, selector)

        when {
            module != null -> {
                modules.logger.info("resolved dependency from ${name}${consumerProperty.name} to ${providerClass.simpleName} ")
                return true
            }

            optional -> {
                modules.logger.info("unable to resolve optional dependency from ${name}${consumerProperty.name} to ${providerClass.simpleName} ")
                return true
            }

            else -> {
                modules.logger.error("unable to resolve dependency from ${name}${consumerProperty.name} to ${providerClass.simpleName} ")
                return false
            }
        }
    }

}