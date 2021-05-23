/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.examples.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

@Serializable
data class SimpleExampleDto(

    override var id: EntityId<SimpleExampleDto>,
    var name: String

) : EntityBo<SimpleExampleDto> {

    companion object : EntityBoCompanion<SimpleExampleDto>(boNamespace = "simple-example")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name min 1 max 30 blank false default "Example Name"
    }

}