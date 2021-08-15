/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.cookbook.entity.builtin.ExampleBl
import zakadabar.cookbook.sqlite.bundle.business.ExampleBundleBl
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
            all = PUBLIC
        }

        server += DemoBl()
        server += DemoBlobBl()

        server += ExampleBl()
        server += ExampleBundleBl()
    }

}