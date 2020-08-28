/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.extend

import zakadabar.stack.data.schema.DtoSchema

/**
 * A contract for DTO classes that have an id.
 */
@Suppress("UNCHECKED_CAST")
interface DtoWithRecordContract<T> {

    val id: Long

    fun comm(): RecordRestCommContract<T>

    fun schema() = DtoSchema.NO_VALIDATION

    suspend fun get(id: Long) = comm().get(id)

    suspend fun search(parameters: String) = comm().search(parameters)

    suspend fun create() = comm().create(this as T)

    suspend fun update() = comm().update(this as T)

}