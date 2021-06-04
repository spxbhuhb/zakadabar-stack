/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.server
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : BackendModule {

    override fun onModuleLoad() {
        zakadabar.lib.accounts.backend.install()
        zakadabar.lib.i18n.backend.install()

        server += TestBl()
        server += TestBlobBl()
    }

}