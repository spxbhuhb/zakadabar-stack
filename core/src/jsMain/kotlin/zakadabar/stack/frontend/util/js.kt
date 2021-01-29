/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.newInstance(): T {
    inline fun callCtor(ctor: dynamic) = js("new ctor()")
    return callCtor(this.js) as T
}


@Deprecated("use RecordDtoCompanion<T>.default() instead")
fun <T : RecordDto<T>> KClass<T>.newWithDefaults(): T {
    inline fun callCtor(ctor: dynamic) = js("new ctor()")
    val dto = callCtor(this.js) as T
    dto.schema().setDefaults()
    return dto
}

/**
 * Creates an instance of the given DTO with the default set in the
 * schema.
 *
 * @return an instance of T with the default values set
 */
@Suppress("unused") // we do want to use this on the companion
fun <T : RecordDto<T>> RecordDtoCompanion<T>.default(): T {
    @Suppress("UNUSED_PARAMETER") // it is used actually, but it's a JS hack
    inline fun callCtor(ctor: dynamic) = js("new ctor()")
    val dto = callCtor(this::class.js) as T
    dto.schema().setDefaults()
    return dto
}

external fun encodeURIComponent(encodedURI: String): String