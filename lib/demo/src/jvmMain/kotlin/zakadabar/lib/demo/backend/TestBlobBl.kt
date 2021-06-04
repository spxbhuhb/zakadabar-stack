/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.lib.blobs.backend.BlobBlBase
import zakadabar.lib.demo.data.TestBlob
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer

class TestBlobBl : BlobBlBase<TestBlob>(
    TestBlob::class,
    TestBlobExposedPa()
) {
    override val authorizer: Authorizer<TestBlob> = SimpleRoleAuthorizer {
        allReads = StackRoles.siteMember
        allWrites = StackRoles.siteAdmin
    }
}
