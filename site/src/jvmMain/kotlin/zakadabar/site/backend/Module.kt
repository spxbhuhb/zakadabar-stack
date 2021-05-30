/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend

import zakadabar.lib.examples.backend.builtin.BuiltinBl
import zakadabar.lib.examples.backend.builtin.ExampleReferenceBl
import zakadabar.lib.examples.backend.data.SimpleExampleBl
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.authorize.UnsafeAuthorizer
import zakadabar.stack.backend.custom.ContentBackend
import zakadabar.stack.backend.server
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : BackendModule {

    override fun onModuleLoad() {
        server += ContentBackend("content")

        server += BuiltinBl()
        server += ExampleReferenceBl()

        UnsafeAuthorizer.enabled = true
        server += SimpleExampleBl()
    }

}