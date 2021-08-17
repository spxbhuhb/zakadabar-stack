/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema


/**
 * Business Object of DemoBo.
 * 
 * Generated with Bender at 2021-06-04T02:35:21.580Z.
 *
 * Please do not implement business logic in this class. If you add fields,
 * please check the frontend table and form, and also the persistence API on 
 * the backend.
 */
@Serializable
class DemoBo(

    override var id : EntityId<DemoBo>,
    var name : String,
    var value : Int

) : EntityBo<DemoBo> {

    companion object : EntityBoCompanion<DemoBo>("demo")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name blank false
        + ::value 
    }

}