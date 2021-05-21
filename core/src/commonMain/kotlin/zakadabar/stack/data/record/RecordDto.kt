/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.DtoBase

@Suppress("UNCHECKED_CAST")
@Deprecated("use EntityBo instead", ReplaceWith("EntityBo", imports = arrayOf("zakadabar.stack.data.entity")))
interface RecordDto<T> : DtoBase, BaseBo {

    var id: RecordId<T>

    fun getDtoNamespace(): String

    fun comm(): RecordCommInterface<T>

    suspend fun create() = comm().create(this as T)

    suspend fun update() = comm().update(this as T)

    suspend fun delete() = comm().delete(id)

}