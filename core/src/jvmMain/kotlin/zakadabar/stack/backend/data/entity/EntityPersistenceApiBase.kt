/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.entity

import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId

open class EntityPersistenceApiBase<T : EntityBo<T>> {

    open fun onModuleLoad() {

    }

    open fun list(): List<T> {
        throw NotImplementedError()
    }

    open fun create(bo: T): T {
        throw NotImplementedError()
    }

    open fun read(entityId: EntityId<T>): T {
        throw NotImplementedError()
    }

    open fun update(bo: T): T {
        throw NotImplementedError()
    }

    open fun delete(entityId: EntityId<T>) {
        throw NotImplementedError()
    }


}