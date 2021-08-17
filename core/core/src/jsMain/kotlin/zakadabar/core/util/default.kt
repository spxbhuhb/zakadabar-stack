/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

import kotlin.reflect.KClass

/**
 * Creates a new instance of the given class.
 */
actual inline fun <reified T : Any> KClass<T>.newInstance(): T = callCtor(this.js) as T

@Suppress("UNUSED_PARAMETER") // it is used actually, but it's a JS hack
fun callCtor(ctor: dynamic) = js("new ctor()")
