/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend.sub

import zakadabar.lib.content.data.sub.ContentCategoryBo
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.EmptyAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase

/**
 * Business Logic for ContentCategoryBo.
 * 
 * Generated with Bender at 2021-06-05T02:31:08.497Z.
 */
open class ContentCategoryBl : EntityBusinessLogicBase<ContentCategoryBo>(
    boClass = ContentCategoryBo::class
) {

    override val pa = ContentCategoryExposedPaGen()

    override val authorizer : Authorizer<ContentCategoryBo> = EmptyAuthorizer()
    
}