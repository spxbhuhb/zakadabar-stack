/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.extend

interface RecordRestCommContract<T> {

    suspend fun get(id: Long): T

    suspend fun get(id: Long?): T?
    
    suspend fun search(parameters: String): List<T>

    suspend fun create(dto: T): T

    suspend fun update(dto: T): T

}