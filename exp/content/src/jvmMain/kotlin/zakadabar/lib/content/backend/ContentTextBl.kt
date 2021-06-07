/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.content.data.ContentTextBo
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.EmptyAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase

/**
 * Business Logic for ContentTextBo.
 * 
 * Generated with Bender at 2021-06-07T02:56:36.423Z.
 */
open class ContentTextBl : EntityBusinessLogicBase<ContentTextBo>(
    boClass = ContentTextBo::class
) {

    override val pa = ContentTextExposedPaGen()

    override val authorizer : Authorizer<ContentTextBo> = EmptyAuthorizer()
    
}