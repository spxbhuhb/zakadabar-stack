/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

import kotlin.reflect.KClass

open class InstanceStore<IT : Any> {

    protected val instances = mutableListOf<IT>()

    open operator fun plusAssign(instance: IT) {
        instances.add(instance)
    }

    /**
     * Find a instance of the given class. The class may be an interface.
     *
     * @param    T      The class to look for
     *
     * @return   First instance of [T] from the instances.
     *
     * @throws   NoSuchElementException   when there is no such instance
     */
    inline fun <reified T : Any> first() = first(T::class)

    /**
     * Find a instance of the given class. The class may be an interface.
     *
     * @param    kClass      The class to look for
     *
     * @return   First instance of [kClass] from the instances.
     *
     * @throws   NoSuchElementException   when there is no such instance
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
     * @return   First instance of [kClass] from the instances.
     *
     * @throws   NoSuchElementException   when there is no such instance
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
     * @return   First instance of [T] from the instances or null.
     */
    inline fun <reified T : Any> firstOrNull() = firstOrNull(T::class)

    /**
     * Find a instance of the given class, return with null when not found.
     * The class may be an interface.
     *
     * @param    kClass      The class to look for
     *
     * @return   First instance of [kClass] from the instances or null.
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
     * @return   First instance of [kClass] from the instances or null.
     */
    open fun <T : Any> firstOrNull(kClass: KClass<T>, selector: (T) -> Boolean): T? {
        @Suppress("UNCHECKED_CAST") // checking for class
        return instances.firstOrNull { kClass.isInstance(it) && selector(it as T) } as? T
    }
}