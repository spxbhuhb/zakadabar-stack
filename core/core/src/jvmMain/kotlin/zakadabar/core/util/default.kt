/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

import zakadabar.core.data.BaseBo
import zakadabar.core.data.entity.EntityBo
import zakadabar.core.data.entity.EntityBoCompanion
import kotlin.reflect.KClass

actual inline fun <reified T : Any> KClass<T>.newInstance() = T::class.java.getDeclaredConstructor().newInstance()

/**
 * Creates an instance of the given bo with defaults from the schema.
 *
 * @return an instance of T with the default values set
 */
@Suppress("unused") // we do want to use this on the companion
inline fun <reified T : EntityBo<T>> EntityBoCompanion<T>.default(): T {
    val instance = T::class.java.getDeclaredConstructor().newInstance()
    instance.schema().setDefaults()
    return instance
}

/**
 * Creates an instance of the given bo with defaults from the schema.
 * Then runs the builder function so the defaults may be customized.
 *
 * @return an instance of T with the default values set
 */
@Suppress("unused") // we do want to use this on the companion
inline fun <reified T : EntityBo<T>> EntityBoCompanion<T>.default(builder: T.() -> Unit): T {
    val instance = T::class.java.getDeclaredConstructor().newInstance()
    instance.schema().setDefaults()
    instance.builder()
    return instance
}

/**
 * Creates an instance of the given bo with defaults from the schema.
 * Then runs the builder function so the defaults may be customized.
 *
 * @return an instance of T with the default values set
 */
@Suppress("unused") // we do want to use this on the companion
actual inline fun <reified T : BaseBo> default(builder: T.() -> Unit): T {
    val instance = T::class.java.getDeclaredConstructor().newInstance()
    instance.schema().setDefaults()
    instance.builder()
    return instance
}