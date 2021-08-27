/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend

import zakadabar.core.authorize.SimpleRoleAuthorizerProvider
import zakadabar.core.route.RoutedModule
import zakadabar.core.server.server
import zakadabar.core.server.util.ContentBackend
import zakadabar.core.util.PublicApi
import zakadabar.lib.examples.backend.builtin.BuiltinBl
import zakadabar.lib.examples.backend.builtin.ExampleReferenceBl
import zakadabar.lib.examples.backend.data.SimpleExampleBl
import zakadabar.site.backend.business.RecipeBl

@PublicApi
object Module : RoutedModule {

    override fun onModuleLoad() {
        server += ContentBackend("content")
        server += RecipeBl()

        server += SimpleRoleAuthorizerProvider {
            all = PUBLIC
        }

        server += BuiltinBl()
        server += ExampleReferenceBl()
        server += zakadabar.cookbook.entity.builtin.ExampleReferenceBl()
        server += SimpleExampleBl()
    }

}