/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.core.authorize.SimpleRoleAuthorizerProvider
import zakadabar.core.module.modules
import zakadabar.core.route.RoutedModule
import zakadabar.core.server.server
import zakadabar.core.util.PublicApi
import zakadabar.lib.examples.backend.builtin.BuiltinBl
import zakadabar.lib.examples.backend.builtin.ExampleReferenceBl
import zakadabar.lib.schedule.business.WorkerBl

@PublicApi
object Module : RoutedModule {

    override fun onModuleLoad() {
        zakadabar.lib.accounts.install()
        zakadabar.lib.i18n.install()

        server += SimpleRoleAuthorizerProvider {
            all = LOGGED_IN
        }

        server += DemoBl()
        server += DemoBlobBl()

        server += ExampleReferenceBl()
        server += BuiltinBl()

        zakadabar.lib.schedule.install()
        modules += WorkerBl("worker1")

        zakadabar.lib.email.install()
    }

}