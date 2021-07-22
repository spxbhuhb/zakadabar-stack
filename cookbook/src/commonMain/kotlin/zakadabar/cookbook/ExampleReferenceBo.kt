/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

@Serializable
data class ExampleReferenceBo(

    override var id: EntityId<ExampleReferenceBo>,
    var name: String

) : EntityBo<ExampleReferenceBo> {

    companion object : EntityBoCompanion<ExampleReferenceBo>("zkc-example-reference")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name max 50
    }

}