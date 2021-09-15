/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

import kotlin.reflect.KClass

actual fun <T : Any> KClass<T>.newInstance(): T = this.java.getDeclaredConstructor().newInstance()

