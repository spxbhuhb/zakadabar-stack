/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.extend

import io.ktor.features.*
import io.ktor.http.*
import zakadabar.stack.util.Executor

interface RestBackend<T> {

    fun query(executor: Executor, id: Long? = null, parentId: Long? = null, parameters: Parameters): List<T>

    fun fetch(executor: Executor, id: Long, parameters: Parameters): T {
        val results = query(executor, id, null, parameters)
        if (results.isEmpty()) throw NotFoundException()
        return results.first()
    }

    fun create(executor: Executor, dto: T): T

    fun update(executor: Executor, dto: T): T

}