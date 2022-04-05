/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend

import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.cookbook.entity.builtin.ExampleReferenceBl
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.authorize.SimpleRoleAuthorizerProvider
import zakadabar.core.data.EntityId
import zakadabar.core.module.modules
import zakadabar.core.route.RoutedModule
import zakadabar.core.server.util.ContentBackend
import zakadabar.core.util.PublicApi
import zakadabar.lib.examples.backend.builtin.BuiltinBl
import zakadabar.lib.examples.backend.data.SimpleExampleBl
import zakadabar.lib.lucene.business.LuceneBl
import zakadabar.site.backend.business.RecipeBl

@PublicApi
object Module : RoutedModule {

    override fun onModuleLoad() {
        modules += ContentBackend("content")
        modules += RecipeBl()

        modules += SimpleRoleAuthorizerProvider {
            all = PUBLIC
        }

        // these are from lib:examples
        modules += SimpleExampleBl()
        modules += BuiltinBl()
        modules += zakadabar.lib.examples.backend.builtin.ExampleReferenceBl()

        modules += LuceneBl()

        zakadabar.cookbook.install()
    }

    override fun onModuleStart() {
        transaction {
            val pa = modules.first<ExampleReferenceBl>().pa

            if (pa.list().size < 10) {
                (1..10).forEach {
                    pa.create(ExampleReferenceBo(EntityId(), "ref $it"))
                }
            }
        }
    }
}