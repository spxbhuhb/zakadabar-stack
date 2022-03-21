/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.entity.builtin

import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

@Serializable
data class SmallExampleBo(

    override var id: EntityId<SmallExampleBo>,
    var enumSelectValue: ExampleEnum,
    var stringValue: String

) : EntityBo<SmallExampleBo> {

    companion object : EntityBoCompanion<SmallExampleBo>("zkc-small-example")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    @Suppress("DuplicatedCode")
    override fun schema() = BoSchema {
        + ::enumSelectValue
        + ::stringValue blank false max 50
    }

}