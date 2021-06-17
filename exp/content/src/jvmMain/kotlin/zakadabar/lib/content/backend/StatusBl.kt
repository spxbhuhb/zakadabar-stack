/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.content.data.StatusBo
import zakadabar.stack.backend.business.EntityBusinessLogicBase

/**
 * Business Logic for StatusBo.
 * 
 * Generated with Bender at 2021-06-05T02:07:27.691Z.
 */
open class StatusBl : EntityBusinessLogicBase<StatusBo>(
    boClass = StatusBo::class
) {

    override val pa = StatusExposedPaGen()

    override val authorizer by provider()

}