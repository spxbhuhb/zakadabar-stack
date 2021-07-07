/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer.Companion.LOGGED_IN
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizerProvider
import zakadabar.stack.backend.server
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : RoutedModule {

    override fun onModuleLoad() {
        zakadabar.lib.accounts.backend.install()
        zakadabar.lib.i18n.backend.install()
        zakadabar.lib.content.backend.install()

        server += SimpleRoleAuthorizerProvider {
            all = LOGGED_IN
        }

        server += TestBl()
        server += TestBlobBl()
    }

}