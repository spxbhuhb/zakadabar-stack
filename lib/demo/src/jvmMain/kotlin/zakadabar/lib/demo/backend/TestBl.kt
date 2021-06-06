/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.lib.demo.data.TestBo
import zakadabar.stack.backend.authorize.provider
import zakadabar.stack.backend.business.EntityBusinessLogicBase

/**
 * Business Logic for TestBo.
 * 
 * Generated with Bender at 2021-06-04T02:35:21.586Z.
 */
open class TestBl : EntityBusinessLogicBase<TestBo>(
    boClass = TestBo::class
) {

    override val pa = TestExposedPaGen()

    override val authorizer by provider()
    
}