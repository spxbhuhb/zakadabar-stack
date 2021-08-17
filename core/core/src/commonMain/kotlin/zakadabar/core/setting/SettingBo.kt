/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.setting

import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

@Serializable
class SettingBo(
    override var id: EntityId<SettingBo>,
    var source: SettingSource,
    var namespace: String,
    var className: String,
    var descriptor: String?
) : EntityBo<SettingBo> {

    companion object : EntityBoCompanion<SettingBo>("setting")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::source
        + ::namespace min 0 max 100
        + ::className min 1 max 100 blank false
    }
}