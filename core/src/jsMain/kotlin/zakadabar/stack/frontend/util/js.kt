/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

import zakadabar.stack.data.record.RecordDto
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.newInstance(): T {
    inline fun callCtor(ctor: dynamic) = js("new ctor()")
    return callCtor(this.js) as T
}


fun <T : RecordDto<T>> KClass<T>.newWithDefaults(): T {
    inline fun callCtor(ctor: dynamic) = js("new ctor()")
    val dto = callCtor(this.js) as T
    dto.schema().setDefaults()
    return dto
}

external fun encodeURIComponent(encodedURI: String): String