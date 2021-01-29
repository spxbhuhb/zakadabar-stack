/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.util

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion

/**
 * Creates an instance of the given DTO with the default set in the
 * schema.
 *
 * @return an instance of T with the default values set
 */
@Suppress("unused") // we do want to use this on the companion
inline fun <reified T : RecordDto<T>> RecordDtoCompanion<T>.default(): T {
    val instance = T::class.java.newInstance()
    instance.schema().setDefaults()
    return instance
}