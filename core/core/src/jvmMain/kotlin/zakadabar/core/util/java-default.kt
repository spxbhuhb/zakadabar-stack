/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

import kotlin.reflect.KClass

actual inline fun <reified T : Any> KClass<T>.newInstance(): T = T::class.java.getDeclaredConstructor().newInstance()

