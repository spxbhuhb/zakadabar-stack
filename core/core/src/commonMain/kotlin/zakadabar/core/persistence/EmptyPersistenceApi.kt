/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.persistence

import zakadabar.core.data.entity.EntityBo

/**
 * This persistence API simply skips everything.
 */
class EmptyPersistenceApi<T : EntityBo<T>> : EntityPersistenceApi<T> {

    override fun <R> withTransaction(func: () -> R) = func()

    override fun commit() {

    }

    override fun rollback() {

    }

}
