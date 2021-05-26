/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.examples.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema


/**
 * Business Object of SimpleExampleBo.
 *
 * Generated with Bender at 2021-05-25T05:26:31.488Z.
 *
 * Please do not implement business logic in this class. If you add fields,
 * please check the frontend table and form, and also the persistence API on
 * the backend.
 */
@Serializable
class SimpleExampleBo(

    override var id: EntityId<SimpleExampleBo>,
    var name : String

) : EntityBo<SimpleExampleBo> {

    companion object : EntityBoCompanion<SimpleExampleBo>("zkl-simple-example")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name blank false min 1 max 30
    }

}