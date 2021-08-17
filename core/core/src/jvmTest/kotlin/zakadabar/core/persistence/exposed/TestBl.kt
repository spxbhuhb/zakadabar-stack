/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.persistence.exposed

import zakadabar.core.business.EntityBusinessLogicBase

class TestBl : EntityBusinessLogicBase<TestBo>(
    boClass = TestBo::class
) {

    override val pa = TestPa()

    override val authorizer by provider()

}