/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.crud

import zakadabar.core.data.entity.EntityId


/**
 * Elements capable of editing the BO content implement this interface.
 */
interface ZkCrud<T> {
    fun openAll()
    fun openCreate()
    fun openRead(recordId: EntityId<T>)
    fun openUpdate(recordId: EntityId<T>)
    fun openDelete(recordId: EntityId<T>)
}