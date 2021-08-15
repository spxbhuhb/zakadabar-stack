/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.persistence.exposed

import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

@Serializable
class TestBo(

    override var id: EntityId<TestBo>,
    var name : String

) : EntityBo<TestBo> {

    companion object : EntityBoCompanion<TestBo>("test")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name blank false min 1 max 30
    }

}