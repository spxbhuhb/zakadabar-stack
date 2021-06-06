/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.content.data.ContentBo
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.EmptyAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase

/**
 * Business Logic for ContentBo.
 * 
 * Generated with Bender at 2021-06-05T06:12:00.490Z.
 */
open class ContentBl : EntityBusinessLogicBase<ContentBo>(
    boClass = ContentBo::class
) {

    override val pa = ContentExposedPaGen()

    override val authorizer : Authorizer<ContentBo> = EmptyAuthorizer()
    
}