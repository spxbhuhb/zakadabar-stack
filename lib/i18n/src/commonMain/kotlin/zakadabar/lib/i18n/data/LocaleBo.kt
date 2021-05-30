/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * A locale like "hu-HU".
 *
 * @property  id            Id of the locale record.
 * @property  name          Name of the locale.
 * @property  description   Description of the locale.
 */
@Serializable
data class LocaleBo(
    override var id: EntityId<LocaleBo>,
    var name: String,
    var description: String
) : EntityBo<LocaleBo> {

    companion object : EntityBoCompanion<LocaleBo>("locale")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::name min 2 max 100 blank false
        + ::description
    }
}