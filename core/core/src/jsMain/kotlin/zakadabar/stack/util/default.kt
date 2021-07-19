/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util

import zakadabar.stack.data.BaseBo
import kotlin.reflect.KClass

external fun decodeURIComponent(encodedURI: String): String

/**
 * Creates a new instance of the given class.
 */
fun <T : Any> KClass<T>.newInstance(): T = callCtor(this.js) as T

/**
 * Creates an instance of the given Bo with the default set in the
 * schema and runs the supplied [builder] function on it to tune
 * field values.
 *
 * @param builder  The builder function to run on the created instance.
 *
 * @return an instance of T with the default values set and modifications made by [builder]
 */
actual inline fun <reified T : BaseBo> default(builder: T.() -> Unit): T {
    val bo = callCtor(T::class.js) as T
    bo.schema().setDefaults()
    bo.builder()
    return bo
}

/**
 * Creates an instance of the given BO with the default set in the
 * schema.
 *
 * @return an instance of T with the default values set
 */
inline fun <reified T : BaseBo> default(): T {
    val bo = callCtor(T::class.js) as T
    bo.schema().setDefaults()
    return bo
}


@Suppress("UNUSED_PARAMETER") // it is used actually, but it's a JS hack
fun callCtor(ctor: dynamic) = js("new ctor()")

/**
 * This is a javascript standard function.
 * // TODO IIRC there is a Ktor Client implementation for this that is MPP
 */
external fun encodeURIComponent(encodedURI: String): String