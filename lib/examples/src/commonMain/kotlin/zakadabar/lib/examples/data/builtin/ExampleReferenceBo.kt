/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.core.data.entity.EntityBo
import zakadabar.core.data.entity.EntityBoCompanion
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.schema.BoSchema

@Serializable
data class ExampleReferenceBo(

    override var id: EntityId<ExampleReferenceBo>,
    var name: String

) : EntityBo<ExampleReferenceBo> {

    companion object : EntityBoCompanion<ExampleReferenceBo>("example-reference")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name max 50
    }

}