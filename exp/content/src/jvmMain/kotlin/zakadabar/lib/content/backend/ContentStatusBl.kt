/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.content.data.ContentStatusBo
import zakadabar.stack.backend.business.EntityBusinessLogicBase

/**
 * Business Logic for ContentStatusBo.
 * 
 * Generated with Bender at 2021-06-05T02:07:27.691Z.
 */
open class ContentStatusBl : EntityBusinessLogicBase<ContentStatusBo>(
    boClass = ContentStatusBo::class
) {

    override val pa = ContentStatusExposedPaGen()

    override val authorizer by provider()

}