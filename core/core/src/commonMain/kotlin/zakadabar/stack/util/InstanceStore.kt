/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import kotlin.reflect.KClass

open class InstanceStore<IT : Any> {

    open val instances = mutableListOf<IT>()

    operator fun plusAssign(instance: IT) {
        instances.add(instance)
    }

    /**
     * Find a instance of the given class. The class may be an interface.
     *
     * @param    T      The class to look for
     *
     * @return   First instance of [T] from the routing targets.
     *
     * @throws   NoSuchElementException   when there is no such target
     */
    inline fun <reified T : Any> first() = first(T::class)

    /**
     * Find a instance of the given class. The class may be an interface.
     *
     * @param    kClass      The class to look for
     *
     * @return   First instance of [kClass] from the routing targets.
     *
     * @throws   NoSuchElementException   when there is no such target
     */
    open fun <T : Any> first(kClass: KClass<T>): T {
        @Suppress("UNCHECKED_CAST") // checking for class
        return instances.first { kClass.isInstance(it) } as T
    }

    /**
     * Find a instance of the given class with a selector method called
     * to decided if the instance is desired. The class may be an interface.
     *
     * @param    kClass      The class to look for
     * @param    selector    Function to select the instance.
     *
     * @return   First instance of [kClass] from the server modules.
     *
     * @throws   NoSuchElementException   when there is no such module
     */
    open fun <T : Any> first(kClass: KClass<T>, selector: (T) -> Boolean): T {
        @Suppress("UNCHECKED_CAST") // checking for class
        return instances.first { kClass.isInstance(it) && selector(it as T) } as T
    }

    /**
     * Find a instance of the given class, return with null when not found.
     * The class may be an interface.
     *
     * @param    T      The class to look for
     *
     * @return   First instance of [T] from the server modules or null.
     */
    inline fun <reified T : Any> firstOrNull() = firstOrNull(T::class)

    /**
     * Find a instance of the given class, return with null when not found.
     * The class may be an interface.
     *
     * @param    kClass      The class to look for
     *
     * @return   First instance of [kClass] from the server modules or null.
     */
    open fun <T : Any> firstOrNull(kClass: KClass<T>): T? {
        @Suppress("UNCHECKED_CAST") // checking for class
        return instances.firstOrNull { kClass.isInstance(it) } as? T
    }

    /**
     * Find a instance of the given class with a selector method called
     * to decided if the instance is desired. The class may be an interface.
     * Return with null when not found.
     *
     * @param    kClass      The class to look for
     * @param    selector    Function to select the instance.
     *
     * @return   First instance of [kClass] from the server modules or null.
     */
    open fun <T : Any> firstOrNull(kClass: KClass<T>, selector: (T) -> Boolean): T? {
        @Suppress("UNCHECKED_CAST") // checking for class
        return instances.firstOrNull { kClass.isInstance(it) && selector(it as T) } as? T
    }
}