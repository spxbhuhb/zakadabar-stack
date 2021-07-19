/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.sqlite.bundle

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * Business Object of ExampleBo.
 *
 * Generated with Bender at 2021-07-18T08:23:07.120Z.
 *
 * Please do not implement business logic in this class. If you add fields,
 * please check the frontend table and form, and also the persistence API on
 * the backend.
 */
@Serializable
class ExampleBo(

    override var id : EntityId<ExampleBo>,
    var c1 : String,
    var c2 : Int

) : EntityBo<ExampleBo> {

    companion object : EntityBoCompanion<ExampleBo>("example")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::c1 max 20
        + ::c2
    }

}