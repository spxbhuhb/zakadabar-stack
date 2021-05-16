/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.util

import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

/**
 * Creates an instance of the given DTO with defaults from the schema.
 *
 * @return an instance of T with the default values set
 */
@Suppress("unused") // we do want to use this on the companion
inline fun <reified T : RecordDto<T>> RecordDtoCompanion<T>.default(): T {
    val instance = T::class.java.getDeclaredConstructor().newInstance()
    instance.schema().setDefaults()
    return instance
}

/**
 * Creates an instance of the given DTO with defaults from the schema.
 * Then runs the builder function so the defaults may be customized.
 *
 * @return an instance of T with the default values set
 */
@Suppress("unused") // we do want to use this on the companion
inline fun <reified T : RecordDto<T>> RecordDtoCompanion<T>.default(builder: T.() -> Unit): T {
    val instance = T::class.java.getDeclaredConstructor().newInstance()
    instance.schema().setDefaults()
    instance.builder()
    return instance
}

/**
 * Creates an instance of the given DTO with defaults from the schema.
 * Then runs the builder function so the defaults may be customized.
 *
 * @return an instance of T with the default values set
 */
@Suppress("unused") // we do want to use this on the companion
inline fun <reified T : DtoBase> default(builder: T.() -> Unit = { }): T {
    val instance = T::class.java.getDeclaredConstructor().newInstance()
    instance.schema().setDefaults()
    instance.builder()
    return instance
}