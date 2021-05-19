/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.crud

import zakadabar.stack.data.record.RecordId

/**
 * Elements capable of editing the DTO content implement this interface.
 */
interface ZkCrud<T> {
    fun openAll()
    fun openCreate()
    fun openRead(recordId: RecordId<T>)
    fun openUpdate(recordId: RecordId<T>)
    fun openDelete(recordId: RecordId<T>)
}