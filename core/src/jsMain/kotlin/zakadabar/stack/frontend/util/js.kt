/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

import zakadabar.stack.data.record.RecordDto
import kotlin.reflect.KClass

/**
 * Creates a new instance of the given class.
 */
fun <T : Any> KClass<T>.newInstance(): T = callCtor(this.js) as T

/**
 * Creates an instance of the given DTO with the default set in the
 * schema and runs the supplied [builder] function on it to tune
 * field values.
 *
 * @param builder  The builder function to run on the created instance.
 *
 * @return an instance of T with the default values set and modifications made by [builder]
 */
inline fun <reified T : RecordDto<T>> default(builder: T.() -> Unit): T {
    val dto = callCtor(T::class.js) as T
    dto.schema().setDefaults()
    dto.builder()
    return dto
}

/**
 * Creates an instance of the given DTO with the default set in the
 * schema.
 *
 * @return an instance of T with the default values set
 */
inline fun <reified T : RecordDto<T>> default(): T {
    val dto = callCtor(T::class.js) as T
    dto.schema().setDefaults()
    return dto
}


@Suppress("UNUSED_PARAMETER") // it is used actually, but it's a JS hack
fun callCtor(ctor: dynamic) = js("new ctor()")

/**
 * This is a javascript standard function.
 * // TODO IIRC there is a Ktor Client implementation for this that is MPP
 */
external fun encodeURIComponent(encodedURI: String): String