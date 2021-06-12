/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.content.data.StereotypeBo
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.data.entity.EntityId

/**
 * Business Logic for StereotypeBo.
 * 
 * Generated with Bender at 2021-06-10T04:08:05.310Z.
 */
open class StereotypeBl : EntityBusinessLogicBase<StereotypeBo>(
    boClass = StereotypeBo::class
) {

    override val pa = StereotypeExposedPa()

    override val authorizer by provider()

    fun getId(locale : EntityId<LocaleBo>, localizedName : String) =
        pa.getId(locale, localizedName)

}