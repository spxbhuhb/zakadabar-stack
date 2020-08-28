/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.extend

import zakadabar.stack.util.Executor

interface EntityRestBackend<T> {

    fun query(executor: Executor, id: Long? = null, parentId: Long? = null): List<T>

    fun create(executor: Executor, dto: T): T

    fun update(executor: Executor, dto: T): T

}