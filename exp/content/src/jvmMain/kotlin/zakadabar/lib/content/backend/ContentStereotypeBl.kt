/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.content.data.ContentStereotypeBo
import zakadabar.stack.backend.business.EntityBusinessLogicBase

/**
 * Business Logic for ContentStereotypeBo.
 * 
 * Generated with Bender at 2021-06-10T04:08:05.310Z.
 */
open class ContentStereotypeBl : EntityBusinessLogicBase<ContentStereotypeBo>(
    boClass = ContentStereotypeBo::class
) {

    override val pa = ContentStereotypeExposedPaGen()

    override val authorizer by provider()

    fun byKey(key : String) = pa.byKey(key)

}