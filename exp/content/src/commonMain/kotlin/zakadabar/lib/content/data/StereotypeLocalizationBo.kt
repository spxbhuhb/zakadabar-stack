/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * Localization of a stereotype name.
 */
@Serializable
class StereotypeLocalizationBo(

    var locale : EntityId<LocaleBo>,
    var localizedName : String

) : BaseBo {

    override fun schema() = BoSchema {
        + ::locale
        + ::localizedName blank false min 2 max 100
    }

}
