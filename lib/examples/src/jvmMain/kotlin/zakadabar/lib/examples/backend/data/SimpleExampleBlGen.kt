/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.stack.backend.authorize.UnsafeAuthorizer
import zakadabar.stack.backend.data.entity.EntityBusinessLogicBase

/**
 * Business Logic for SimpleExampleBo.
 * 
 * Generated with Bender at 2021-05-25T05:35:31.978Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other functions, please extend with `Gen` removed from the name.
 */
class SimpleExampleBlGen : EntityBusinessLogicBase<SimpleExampleBo>(
    boClass = SimpleExampleBo::class
) {

    override val pa = SimpleExampleExposedPaGen()

    override val authorizer = UnsafeAuthorizer<SimpleExampleBo>()

}