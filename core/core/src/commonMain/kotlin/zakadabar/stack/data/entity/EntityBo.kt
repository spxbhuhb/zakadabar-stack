/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import zakadabar.stack.data.BaseBo

@Suppress("UNCHECKED_CAST")
interface EntityBo<T> : BaseBo {

    var id: EntityId<T>

    fun getBoNamespace(): String

    fun comm(): EntityCommInterface<T>

    suspend fun create() = comm().create(this as T)

    suspend fun update() = comm().update(this as T)

    suspend fun update(func : T.() -> Unit) {
        (this as T).func()
        update()
    }

    suspend fun delete() = comm().delete(id)

}