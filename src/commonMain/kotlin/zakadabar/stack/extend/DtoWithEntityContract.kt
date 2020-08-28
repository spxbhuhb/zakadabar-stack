/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.extend

import zakadabar.stack.data.entity.EntityDto

/**
 * A contract for DTO classes that have a corresponding entity in the entity tree.
 */
interface DtoWithEntityContract<T> : DtoWithRecordContract<T> {

    val entityDto: EntityDto?

    fun getType(): String

}