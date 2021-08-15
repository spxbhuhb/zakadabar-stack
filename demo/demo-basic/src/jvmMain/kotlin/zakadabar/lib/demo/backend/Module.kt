/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.lib.examples.backend.builtin.BuiltinBl
import zakadabar.lib.examples.backend.builtin.ExampleReferenceBl
import zakadabar.core.route.RoutedModule
import zakadabar.core.authorize.SimpleRoleAuthorizerProvider
import zakadabar.core.server.server
import zakadabar.core.util.PublicApi

@PublicApi
object Module : RoutedModule {

    override fun onModuleLoad() {
        zakadabar.lib.accounts.backend.install()
        zakadabar.lib.i18n.backend.install()

        server += SimpleRoleAuthorizerProvider {
            all = LOGGED_IN
        }

        server += DemoBl()
        server += DemoBlobBl()

        server += ExampleReferenceBl()
        server += BuiltinBl()
    }

}