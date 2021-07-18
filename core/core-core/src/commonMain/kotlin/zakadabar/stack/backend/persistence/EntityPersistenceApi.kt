/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.persistence

import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId

/**
 * Entity persistence APIs
 */
interface EntityPersistenceApi<T : EntityBo<T>> {

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    fun onModuleLoad() {

    }

    // -------------------------------------------------------------------------
    // Transaction handling
    // -------------------------------------------------------------------------

    /**
     * [EntityBusinessLogicBase] calls this function to ensure that the operation is inside a
     * SQL transaction. For Exposed this is the equivalent of `transaction`. If you would like
     * to handle your transactions manually, you can implement a method that just calls
     * [func] without a surrounding `transaction`. In this case pay attention to cross-BL calls.
     */
    fun <R> withTransaction(func : () -> R) : R

    fun commit()

    fun rollback()

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    fun list(): List<T> {
        throw NotImplementedError()
    }

    fun create(bo: T): T {
        throw NotImplementedError()
    }

    fun read(entityId: EntityId<T>): T {
        throw NotImplementedError()
    }

    fun update(bo: T): T {
        throw NotImplementedError()
    }

    fun delete(entityId: EntityId<T>) {
        throw NotImplementedError()
    }


}