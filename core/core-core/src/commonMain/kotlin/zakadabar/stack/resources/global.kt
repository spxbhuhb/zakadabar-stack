/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.resources

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

lateinit var localizedStrings: ZkBuiltinStrings

/**
 * Translates the normalized name of the given type from the string store.
 * Returns with the type name if there is no translation in the string store.
 */
inline fun <reified T> localized() = localizedStrings.getNormalized(T::class.simpleName!!)

/**
 * Translates the given string to the localized version from the string store.
 */
inline val String.localized
    get() = localizedStrings[this]

/**
 * Translates the normalized name of the given class from the string store.
 * Returns with the class name if there is no translation in the string store.
 */
inline val KClass<*>.localized
    get() = localizedStrings.getNormalized(this)

/**
 * Translates the normalized name of the given property from the string store.
 * Returns with the property name if there is no translation in the string store.
 */
inline val KMutableProperty<*>.localized
    get() = localizedStrings.getNormalized(this.name)