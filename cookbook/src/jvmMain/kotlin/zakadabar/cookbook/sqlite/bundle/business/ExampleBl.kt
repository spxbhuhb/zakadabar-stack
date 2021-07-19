/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.sqlite.bundle.business

import zakadabar.cookbook.sqlite.bundle.ExampleBo
import zakadabar.cookbook.sqlite.bundle.persistence.ExamplePa
import zakadabar.stack.backend.business.EntityBusinessLogicBase

/**
 * Business Logic for ExampleBo.
 * 
 * Generated with Bender at 2021-07-19T02:06:15.096Z.
 */
open class ExampleBl : EntityBusinessLogicBase<ExampleBo>(
    boClass = ExampleBo::class
) {

    override val pa = ExamplePa()

    override val authorizer by provider()
    
}