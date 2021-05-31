/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import kotlin.reflect.KProperty

private object UNINITIALIZED

/**
 * Use this method when the initialization of a property depends
 * on the initialization of another, open property.
 *
 * In those cases the compiler gives a warning, because in extending
 * classes may override the open value.
 */
fun <T> after(initializer: () -> T) = AfterDelegate(initializer)

/**
 * This is the same as Kotlin's Lazy but writable. It will work fine in JavaScript
 * because of the single-threaded execution model.
 */
class AfterDelegate<T>(
    initializer: () -> T
) {
    private var _value: Any? = UNINITIALIZED
    private var _initializer: (() -> T)? = initializer

    @Suppress("UNCHECKED_CAST") // initializer will be checked
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (_value === UNINITIALIZED) {
            _value = _initializer !!()
            _initializer = null
        }
        return _value as T
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _value = value
    }
}