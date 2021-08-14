/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.module

import kotlin.reflect.KClass

actual fun moduleName(kClass : KClass<*>) = kClass.simpleName ?: throw IllegalStateException("cannot find module name")