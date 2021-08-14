/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

import zakadabar.core.data.BaseBo
import kotlin.reflect.KClass

expect inline fun <reified T : Any> KClass<T>.newInstance(): T

expect inline fun <reified T : BaseBo> default(builder: T.() -> Unit): T
